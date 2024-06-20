import com.apple.foundationdb.KeySelector
import com.apple.foundationdb.KeyValue
import com.apple.foundationdb.LocalityUtil
import com.apple.foundationdb.StreamingMode
import com.apple.foundationdb.tuple.Tuple
import global.genesis.db.DbRecord
import global.genesis.db.DbUtil
import global.genesis.db.alias.aliases.FoundationAlias
import global.genesis.db.engine.fdb.FoundationDBEngine
import global.genesis.db.engine.fdb.data.DefaultFDBCache
import global.genesis.db.engine.fdb.helpers.awaitButDontCancel
import global.genesis.dictionary.Field
import global.genesis.dictionary.Table
import global.genesis.pal.view.repo.helper.set
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.future.future
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

fun getBoundaryKeys(
    tuple: Tuple,
    table: Table
): List<ByteArray> {
    val begin = tuple.range().begin
    val end = KeySelector.lastLessOrEqual(tuple.range().end).add(1).key
    println("${now()} - Getting boundary keys for ${table.name}")
    val boundaryKeysIterator = LocalityUtil.getBoundaryKeys(database, begin, end)
    val boundaryKeys =
        listOf<ByteArray>(begin) + boundaryKeysIterator.asSequence().toList() + listOf<ByteArray>(end)
    boundaryKeysIterator.close()
    println("${now()} - Finished getting boundary keys for ${table.name}")
    return boundaryKeys
}

fun readKeyValues(
    begin: KeySelector,
    end: KeySelector
): CompletableFuture<Pair<List<KeyValue>, ByteArray?>> {
    return database.runAsync { tr ->
        GlobalScope.future {
            val iterator = tr.getRange(begin, end, 10000, false, StreamingMode.WANT_ALL).iterator()
            val list = mutableListOf<KeyValue>()
            var kv: KeyValue? = null
            while (iterator.onHasNext().awaitButDontCancel() == true) {
                kv = iterator.next()
                if (kv != null) {
                    list.add(kv)
                }
            }
            // If there is still work to do, next loop iteration needs to continue from where it finished.
            list to kv?.key
        }
    }
}

fun defaultValueForType(type: global.genesis.dictionary.Field.Type): Any = when (type) {
    Field.Type.STRING -> ""
    Field.Type.ENUM -> ""
    Field.Type.INT -> 0
    Field.Type.SHORT -> 0
    Field.Type.DOUBLE -> 0.0
    Field.Type.LONG -> 0L
    Field.Type.BOOLEAN -> false
    Field.Type.RAW -> ByteArray(0)
    Field.Type.BIGDECIMAL -> BigDecimal(0)
    Field.Type.NANO_TIMESTAMP -> 0L
    Field.Type.DATETIME -> DateTime(0)
    Field.Type.DATE -> DateTime(0)
}

val populateMissingKeys = if (args.contains("--populateMissingKeys")) {
    println("Are you sure you want to populate missing keys with default values? Warning: This cannot be undone")
    "Y".equals(readLine()!!, true)
} else {
    false
}

/**
 * This script scans all key values for every table in FDB and reconstitues the DBRecords
 * from the raw data. It then scans records for any missing columns and prints to standard out.
 * It is to be used as a way to detect data corruption issues like those that were identified as
 * the root cause of https://genesisglobal.atlassian.net/browse/PA-101.
 *
 * If the --populateMissingKeys arg is set, then as each missing key is identified, it will be
 * inserted with null if the value is nullable in the dictionary, the default value if the field
 * defines one, or a hardcoded default if the value is non-nullable and does not provide a default.
 *
 * This is to be used with care, as populating missing keys is a destructive operation with no way to
 * restore the previous FDB state unless a raw key-value dump has been performed first.
 */
val foundationDBEngine = rxDb.storageEngine as FoundationDBEngine
val database = foundationDBEngine.database
val tableSubspace = foundationDBEngine.tableSubspace
val aliasStore = foundationDBEngine.aliasStore
val dictionary = foundationDBEngine.dictionary
for (tableName in dictionary.tables.keys) {
    val records = ConcurrentHashMap<String, Pair<DbRecord, Tuple>>()
    val table = dictionary[tableName]
    val fdbCache = DefaultFDBCache(aliasStore, tableSubspace, dictionary)
    val tuple = fdbCache.getTuple(table, table.primaryKey)

    println("Searching $tableName in FDB for missing key values")

    val boundariesCounter = AtomicLong()
    val recordCounter = AtomicLong()
    val boundaryKeys = getBoundaryKeys(tuple, table)
    println("Total boundary keys found: ${boundaryKeys.size}")
    val semaphore = Semaphore(50)
    GlobalScope.future {
        val tasksList = mutableListOf<Job>()
        for (i in 0 until boundaryKeys.size - 1) {
            tasksList.add(
                launch {
                    semaphore.acquire()
                    var currentBegin = KeySelector.firstGreaterOrEqual(boundaryKeys[i])
                    val currentEnd = KeySelector.lastLessOrEqual(boundaryKeys[i + 1]).add(1)
                    do {
                        val result = readKeyValues(currentBegin, currentEnd).awaitButDontCancel()!!
                        for (kv in result.first) {
                            recordCounter.incrementAndGet()
                            val colTuple = Tuple.fromBytes(kv.key)
                            val subKeyTuple = colTuple.popFront().popBack()
                            val fieldName =
                                aliasStore.resolveFieldName(FoundationAlias.newInstance(colTuple.getBytes(colTuple.size() - 1)))
                            val field = table.getField(fieldName)
                            val fieldType = field!!.type

                            val recordId = subKeyTuple.items.last().toString()
                            val value = DbUtil.rawValueToField(fieldType, kv.value)
                            val record = records.getOrPut(recordId) { DbRecord(tableName) to colTuple.popBack() }
                            record.first[fieldName] = value
                        }
                        val nextBegin = result.second
                        if (nextBegin != null) currentBegin = KeySelector.firstGreaterThan(nextBegin)
                    } while (nextBegin != null)
                    semaphore.release()
                    val progressCounter = boundariesCounter.incrementAndGet()
                    val percentage = (progressCounter.toDouble() * 100.0) / boundaryKeys.size.toDouble()
                    val decimalFormat = DecimalFormat("##.##")
                    println("Total progress: ${decimalFormat.format(percentage)}")
                }
            )
        }
        tasksList.forEach { it.join() }
    }.join()

    val fields: Set<String> = table.fields.keys
    database.run { tr ->
        records.entries.forEach { entry ->
            val missingFields = fields - entry.value.first.columns.keys
            missingFields.forEach {
                println("$tableName record ${entry.key} is missing field $it")
                if (populateMissingKeys) {
                    val field = table.fields[it]!!
                    val value = if (field.nullable) {
                        null
                    } else {
                        field.defaultValue ?: defaultValueForType(field.type)
                    }
                    val aliasForField = aliasStore.getAliasForField(it)
                    val key = entry.value.second.add(aliasForField.toByteArray())
                    DbUtil.setValueFromObject(tr, key.pack(), value)
                }
            }
        }
    }
}

