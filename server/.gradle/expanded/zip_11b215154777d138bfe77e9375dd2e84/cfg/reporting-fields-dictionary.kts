/**
 * System              : Genesis Business Library
 * Sub-System          : reporting Configuration
 * Version             : 1.0
 * Copyright           : (c) Genesis
 * Date                : 25 January 2022
 * Function : Provide fields config for reporting.
 *
 * Modification History
 */

fields {
    field(name = "CREATED_BY", type = STRING)
    field(name = "REPORT_CREATED_ON", type = DATETIME)
    field(name = "REPORT_NAME", type = STRING)
    field(name = "REPORT_DESCRIPTION", type = STRING, maxSize = 10000)
    field(name = "REPORT_ID", type = STRING)
    field(name = "REPORT_DATASOURCE", type = STRING, maxSize = 200)
    field(name = "REPORT_COLUMNS", type = STRING, maxSize = 10000)
}
