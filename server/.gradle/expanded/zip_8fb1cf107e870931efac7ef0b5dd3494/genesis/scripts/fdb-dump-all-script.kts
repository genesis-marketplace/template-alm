import com.apple.foundationdb.KeySelector
import com.apple.foundationdb.KeyValue
import com.apple.foundationdb.LocalityUtil
import com.apple.foundationdb.StreamingMode
import global.genesis.db.engine.fdb.FoundationDBEngine
import org.apache.commons.codec.binary.Hex

fun getBoundaryKeysForDump(): List<ByteArray> {
    val begin = ByteArray(1)
    begin[0] = 0.toByte()
    val end = ByteArray(1)
    end[0] = 255.toByte()

    val boundaryKeysIterator = LocalityUtil.getBoundaryKeys(database, begin, end)
    val boundaryKeys =
        listOf(begin) + boundaryKeysIterator.asSequence().toList() + listOf(end)
    boundaryKeysIterator.close()
    return boundaryKeys
}

fun readKeyValues(
    begin: KeySelector,
    end: KeySelector
): Pair<List<KeyValue>, ByteArray?> {
    return database.run { tr ->
        val iterator = tr.getRange(begin, end, 10000, false, StreamingMode.WANT_ALL).iterator()
        val list = mutableListOf<KeyValue>()
        var kv: KeyValue? = null
        while (iterator.onHasNext().get() == true) {
            kv = iterator.next()
            if (kv != null) {
                list.add(kv)
            }
        }
        // If there is still work to do, next loop iteration needs to continue from where it finished.
        list to kv?.key
    }
}

/**
 * This script dumps all raw key value pairs and prints them to standard out.
 * The format is a Hex encoded key and value on a single line, separated by
 * a comma.
 *
 * To run this script use:
 * `GenesisRun fdb-dump-all-script.kts`
 *
 * To dump to a file (arguably more useful) run:
 * `GenesisRun fdb-dump-all-script.kts > output.txt`
 *
 * If the --clear flag is set, the keys will be wiped after they are written.
 * This is a destructive operation so use with caution.
 */
val foundationDBEngine = rxDb.storageEngine as FoundationDBEngine
val database = foundationDBEngine.database

val boundaryKeys = getBoundaryKeysForDump()
for (i in 0 until boundaryKeys.size - 1) {
    var currentBegin = KeySelector.firstGreaterOrEqual(boundaryKeys[i])
    val currentEnd = KeySelector.lastLessOrEqual(boundaryKeys[i + 1]).add(1)
    do {
        val result = readKeyValues(currentBegin, currentEnd)
        for (kv in result.first) {
            val encodedKey = Hex.encodeHexString(kv.key)
            val encodedValue = Hex.encodeHexString(kv.value)
            println("$encodedKey,$encodedValue")
        }
        val nextBegin = result.second
        if (nextBegin != null) currentBegin = KeySelector.firstGreaterThan(nextBegin)
    } while (nextBegin != null)
}

if (args.contains("--clear")) {
    val begin = ByteArray(1)
    begin[0] = 0.toByte()
    val end = ByteArray(1)
    end[0] = 255.toByte()
    database.run {
        it.clear(begin, end)
    }
}
