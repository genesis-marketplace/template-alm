
dependencies {
    implementation("global.genesis:genesis-process:${properties["genesisVersion"]}")
    implementation("org.apache.kafka:kafka-clients:${properties["kafkaClientVersion"]}")
    implementation(project(path = ":ALM-dictionary-cache", configuration = "codeGen"))
}

description = "alm-fxrate-connector"
