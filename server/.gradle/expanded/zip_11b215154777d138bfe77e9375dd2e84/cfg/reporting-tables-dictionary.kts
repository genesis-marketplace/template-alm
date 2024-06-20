/**
 * System              : Genesis Business Library
 * Sub-System          : reporting Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 25 January 2022
 * Function : Provide table definition config for reporting.
 *
 * Modification History
 */

tables {

    table(name = "SAVED_REPORTS", id = 8000) {
        sequence(REPORT_ID, "RE")
        REPORT_NAME
        REPORT_DESCRIPTION
        REPORT_DATASOURCE
        REPORT_COLUMNS
        CREATED_BY
        REPORT_CREATED_ON
        primaryKey(name = "SAVED_REPORTS_BY_ID", id = 1) {
            REPORT_ID
        }
        indices {
            unique {
                REPORT_NAME
            }
        }
    }

}
