package genesis.scripts.unix.groovy

import global.genesis.dta.dta_dictionary.DtaDictionary
import global.genesis.environment.migration.dictionary.DictionaryMigration
import global.genesis.environment.scripts.ScriptsUtils

println("Starting Dictionary Migration")

def db = ScriptsUtils.initRxDb("migrateDatabase")
def store = db.storageEngine.dictionaryStore

def storedDictionary = store.tryGetDictionary()

if (!(storedDictionary instanceof DtaDictionary)) {
    println("Migration not required")
    System.exit(0)
}

def dtaDictionary = (DtaDictionary) storedDictionary

if(!dtaDictionary.isReady()) {
    println("Migration not required")
    System.exit(0)
}

println("Found dictionary to be migrated; starting migration")

def migrated = new DictionaryMigration().migrate(dtaDictionary)

if (!migrated.isReady()) {
    println("Error while converting dictionary")
    System.exit(1)
}

println("Migration successful; saving dictionary.")


try {
    store.saveDictionary(migrated)
} catch (Exception e) {
    println("Error while saving dictionary, exiting.")
    println(e.getMessage())
    System.exit(1)
}

db.close()
System.exit(0)
