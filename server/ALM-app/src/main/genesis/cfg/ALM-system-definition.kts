/**
 * System              : Genesis Business Library
 * Sub-System          : multi-pro-code-test Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 2022-03-18
 * Function : Provide system definition config for multi-pro-code-test.
 *
 * Modification History
 */
systemDefinition {
    global {
        //CSV pipeline variables
        item(name = "ALM_CSV_UPLOAD_DIR", value = "loadData")
        item(name = "ALM_CSV_UPLOAD_CD_FILE", value = "CDs.csv")

        //Kafka connection variables
        //This listens to a Kafka topic that has FX Rates on it in the following format: "CCY,CCY,Rate"
        item(name = "ALM_KAFKA_BOOTSTRAP_SERVER", value = "b-2.genesisignite.iz7jhl.c4.kafka.eu-west-2.amazonaws.com:9094,b-1.genesisignite.iz7jhl.c4.kafka.eu-west-2.amazonaws.com:9094")
        item(name = "ALM_KAFKA_FX_RATES_TOPIC_NAME", value = "fx-rate")

        //REST API variables
        item(name = "ALM_REST_API_URL_LOGIN", value = "https://playground.demo.genesis.global/gwf/event-login-auth")
        item(name = "ALM_REST_API_URL_QUERY", value = "https://playground.demo.genesis.global/gwf/ALL_LOAN")
        item(name = "ALM_REST_API_USERNAME", value = "JaneDee")
        item(name = "ALM_REST_API_PASSWORD", value = "beONneON*74")
        item(name = "ALM_REST_API_AUTH_PROCESS_NAME", value = "AUTH_MANAGER")
    }

    systems {

    }

}