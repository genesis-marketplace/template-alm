import com.apple.foundationdb.KeySelector
import com.apple.foundationdb.KeyValue
import com.apple.foundationdb.StreamingMode
import com.apple.foundationdb.tuple.Tuple
import com.google.common.collect.Maps
import global.genesis.commons.config.DbConfig
import global.genesis.commons.flake.SharedCounterGFlake.defaultToString
import global.genesis.commons.utils.ConsoleUtils
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
import org.joda.time.format.DateTimeFormat
import java.lang.System
import java.util.concurrent.CompletableFuture
import java.util.concurrent.atomic.AtomicLong

val HEADER_DIVIDER =
    "==========================================================================================="
val DIVIDER = "-------------------------------------------------------------------------------------------"
val DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS Z"
val DATE_FORMAT = "yyyy-MM-dd"

fun buildRecord(
    table: Table,
    fields: Map<String, ByteArray>
): DbRecord {
    val dbRecord = DbRecord(table.name)
    for ((key, value) in fields) {
        val field = table.getField(key) ?: throw IllegalArgumentException(
            "Unknown field $key found when scanning table ${table.name}"
        )
        val convertedValue = DbUtil.rawValueToField(
            field.type,
            value
        )
        dbRecord.setObject(key, convertedValue)
    }
    return dbRecord
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

fun printTableHeader(format: String?, vararg titles: Any?) {
    System.out.printf(format, *titles)
    println(HEADER_DIVIDER)
}

fun printRecord(record: DbRecord, printNullValues: Boolean) {
    var columnName: String
    var value: Any?
    val table: Table = rxDb.dictionary.getTable(record.tableName)!!
    // Order fields to print by name.
    val sortedColumns: MutableMap<String, Any?> = Maps.newTreeMap()
    sortedColumns.putAll(record.columns)
    // Print Timestamp first, as its a mandatory field we know it will always be there
    val printableFields: Set<String> = sortedColumns.keys
    if (printableFields.contains("TIMESTAMP")) {
        if (record.getObject("TIMESTAMP") != null || printNullValues) {
            printTableData(table.getField("TIMESTAMP")!!, record.getObject("TIMESTAMP"))
        }
    }
    for (printableField in printableFields) {
        columnName = printableField
        if (printableFields.contains(columnName) && "TIMESTAMP" != columnName) {
            value = record.getObject(columnName)
            if (value != null || printNullValues) {
                printTableData(table.getField(columnName)!!, value)
            }
        }
    }
    println(DIVIDER)
}

fun printTableData(field: Field, value: Any?) {
    var printableValue = value
    if (field.name != DbConfig.RECORD_ID_FIELD) {
        var type = field.type.name
        if (value == null) {
            if (field.type == Field.Type.ENUM) {
                type = "ENUM[" + field.values + "]"
            }
            printableValue = ""
        } else {
            when (field.type) {
                Field.Type.DATETIME -> printableValue = longToDate(DATETIME_FORMAT, value)
                Field.Type.DATE -> printableValue = longToDate(DATE_FORMAT, value)
                Field.Type.NANO_TIMESTAMP -> printableValue = defaultToString(value.toString().toLong())
                Field.Type.ENUM -> type = "ENUM[" + field.values + "]"
                else -> {}
            }
        }
        val name: String = ellipsize(field.name)
        printableValue = ellipsize(printableValue.toString())
        System.out.printf("%-40s %-40s %-20s%n", name, printableValue, type)
    }
}

fun longToDate(pattern: String, value: Any): String? {
    val sdf = DateTimeFormat.forPattern(pattern)
    return if (value is Long) {
        sdf.print(DateTime(value))
    } else {
        value.toString()
    }
}

fun ellipsize(content: String): String {
    var content = content
    if (content.length > 40) {
        content = content.substring(0, 37) + "..."
    }
    return content
}

/**
 * This script duplicates some of the formatting code from DbMon and prints the input table
 * in the same format as `search 1` would in DbMon. I found it to be a useful utility for visualising
 * discrepancies between data actually in FDB and what the read operations returned.
 */
val foundationDBEngine = rxDb.storageEngine as FoundationDBEngine
val database = foundationDBEngine.database
val tableSubspace = foundationDBEngine.tableSubspace
val aliasStore = foundationDBEngine.aliasStore
val dictionary = foundationDBEngine.dictionary
val fdbCache = DefaultFDBCache(aliasStore, tableSubspace, dictionary)
val tableName = args[0]
val table = dictionary[tableName]
val tuple = fdbCache.getTuple(table, table.primaryKey)

println("Counting $tableName in FDB manually")

val boundariesCounter = AtomicLong()
val recordCounter = AtomicLong()
val boundaryKeys = listOf(tuple.range().begin, KeySelector.lastLessOrEqual(tuple.range().end).add(1).key)

println("Total boundary keys found: ${boundaryKeys.size}")
val semaphore = Semaphore(50)
val records: MutableMap<String, DbRecord> = LinkedHashMap()

GlobalScope.future {
    val tasksList = mutableListOf<Job>()
    for (i in 0 until boundaryKeys.size - 1) {
        tasksList.add(
            launch {
                semaphore.acquire()
                val begin = boundaryKeys[i]
                val end = boundaryKeys[i + 1]
                var currentBegin = KeySelector.firstGreaterOrEqual(begin)
                val currentEnd = KeySelector.lastLessOrEqual(end).add(1)
                val curObjects: MutableMap<String, ByteArray> = hashMapOf()
                val totalFields = table.fields.size
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
                        val record = records.getOrPut(recordId) { DbRecord(tableName) }
                        record[fieldName] = value
                    }
                    val nextBegin = result.second
                    if (nextBegin != null) currentBegin = KeySelector.firstGreaterThan(nextBegin)
                } while (nextBegin != null)
                semaphore.release()
            }
        )
    }
    tasksList.forEach { it.join() }
}.join()

ConsoleUtils.printSimpleHeader(table.name)
printTableHeader("%-40s %-40s %-20s\n", "Field Name", "Value", "Type")

records.entries.forEach { entry ->
    printRecord(entry.value, true)
}
