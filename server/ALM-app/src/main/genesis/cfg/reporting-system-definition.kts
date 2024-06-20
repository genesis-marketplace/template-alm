/**
 * System              : Genesis Business Library
 * Sub-System          : reporting Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 25 January 2022
 * Function : Provide system definition config for reporting.
 *
 * Modification History
 */
systemDefinition {
    global {

        //List out the datasource names that users can use to create reports
        item("REPORTING_DATASOURCE_LIST", listOf("ALL_FX_TRADES", "ALL_ENTITYS", "ALL_BOOKS", "ALL_CLIENTS", "ALL_POSITIONS", "ALL_LOAN_TRADES", "ALL_CD_TRADES", "ALL_FX_RATES"))
    }

    systems {

    }

}