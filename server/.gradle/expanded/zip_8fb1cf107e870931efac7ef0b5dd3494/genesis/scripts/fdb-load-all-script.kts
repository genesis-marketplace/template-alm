import global.genesis.db.engine.fdb.FoundationDBEngine
import org.apache.commons.codec.binary.Hex

/**
 * This script loads raw key value pairs and writes them into FDB.
 * Data is loaded from standard in. The format expected is the same
 * as the format output by fdb-dump-all-script.kts.
 *
 * The easiest way to use this script is to run fdb-dump-all-script.kts
 * first, a la
 * `GenesisRun fdb-dump-all-script.kts > output.txt`
 * then run this script
 * `GenesisRun fdb-load-all-script.kts < output.txt`
 */
val foundationDBEngine = rxDb.storageEngine as FoundationDBEngine
val database = foundationDBEngine.database
var count = 0
try {
    while (true) {
        count++
        val line = readlnOrNull() ?: break
        val split = line.split(",")
        val encodedKey = split[0]
        val encodedValue = split[1]
        val key = Hex.decodeHex(encodedKey)
        val value = Hex.decodeHex(encodedValue)
        database.run { tr ->
            tr.set(key, value)
        }
    }
} catch (e: RuntimeException) {
    println("${e.javaClass.simpleName} caught at line count $count, ${e.message}")
    e.printStackTrace()
}
