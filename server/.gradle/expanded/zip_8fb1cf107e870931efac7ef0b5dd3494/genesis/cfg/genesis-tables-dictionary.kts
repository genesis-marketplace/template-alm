/**
 *
 *   System              : DTA Core Library
 *   Sub-System          : DTA Configuration
 *   Version             : 4.0.0
 *   Copyright           : (c) DTA
 *   Date                : 29th July 2019
 *
 *   Function : Provide Dictionary Config details for DTA.
 *
 *   Modification History
 *
 */

tables {
    table(name = "AUDIT_TRAIL", id = 0) {
        sequence(ID, "AT")
        ENTITY_ID
        AUDIT_CODE
        AUDIT_TEXT
        USER_NAME
        VERSION
        CLIENT_ORDER_ID
        REJECT_REASON
        RX_SEQUENCE
        primaryKey {
            ID
        }
        indices {
            nonUnique {
                ENTITY_ID
            }
            nonUnique {
                USER_NAME
            }
            nonUnique {
                TIMESTAMP
            }
        }
    }
    table(name = "SYSTEM", id = 1) {
        SYSTEM_KEY
        SYSTEM_VALUE
        primaryKey {
            SYSTEM_KEY
        }
    }
    table(name = "USER_SESSION", id = 2) {
        USER_NAME
        SESSION_ID
        START_TIMESTAMP
        LAST_ACCESS_TIME
        SESSION_AUTH_TOKEN
        REFRESH_TOKEN
        TIMEOUT
        REFRESH_TOKEN_TIMEOUT
        HOST
        ATTRIBUTES
        primaryKey {
            SESSION_ID
        }
        indices {
            nonUnique {
                USER_NAME
            }
            unique {
                SESSION_AUTH_TOKEN
            }
            unique {
                REFRESH_TOKEN
            }
        }
    }
    table(name = "NOTIFICATION", id = 4) {
        NOTIFICATION_ID
        PROCESS
        NODE
        TEXT
        NOTIFICATION_CODE
        NOTIFICATION_TIMESTAMP
        ACTIVE
        primaryKey {
            NOTIFICATION_ID
        }
    }
    table(name = "RIGHT_SUMMARY", id = 5) {
        USER_NAME
        RIGHT_CODE
        primaryKey {
            USER_NAME
            RIGHT_CODE
        }
    }
    table(name = "PROCESS_REF", id = 6) {
        SOURCE_ID
        REF
        primaryKey {
            SOURCE_ID
        }
    }
    table(name = "PUBLISHER_SUBSCRIPTION", id = 7) {
        QUERY_NAME
        FIELD_NAME
        FIELD_VALUE
        primaryKey(name = "PUBLISHER_SUBSCRIPTION_BY_QUERY_NAME_FIELD_NAME", id = 1) {
            QUERY_NAME
            FIELD_NAME
            FIELD_VALUE
        }
    }
    table(name = "DYNAMIC_RULE", id = 8, audit = details(id = 9, sequence = "DA", tsKey = true)) {
        sequence(ID, "DR")
        NAME
        DESCRIPTION
        USER_NAME
        RULE_TABLE
        RULE_STATUS
        RULE_EXPRESSION
        PROCESS_NAME
        MESSAGE_TYPE
        RESULT_EXPRESSION
        TABLE_OPERATION
        IS_TEMPLATE
        RULE_EXECUTION_STRATEGY
        RULE_TYPE
        primaryKey {
            ID
        }
        indices {
            nonUnique {
                NAME
            }
        }
    }

    table(name = "DYNAMIC_RULE_TEMPLATE_PARAMS", id = 10) {
        DYNAMIC_RULE_ID
        PARAM_SOURCE
        PARAM_SOURCE_TYPE
        PARAM_TYPE
        PARAM_NAME
        PARAM_LABEL
        PARAM_OPERATOR
        primaryKey {
            DYNAMIC_RULE_ID
            PARAM_NAME
        }
        indices {
            nonUnique {
                DYNAMIC_RULE_ID
            }
        }
    }

    table(name = "DYNAMIC_RULE_EXECUTION", id = 11) {
        DYNAMIC_RULE_ID
        RECORD_ID_REF
        primaryKey {
            DYNAMIC_RULE_ID
            RECORD_ID_REF
        }
        indices {
            nonUnique {
                DYNAMIC_RULE_ID
            }
        }
    }

    table(name = "GENESIS_PROCESS", id = 12) {
        PROCESS_NAME
        PROCESS_STATUS
        PROCESS_STATUS_MESSAGE
        PROCESS_STATE_TEXT
        PROCESS_RESOURCES
        PROCESS_HOSTNAME
        PROCESS_CPU_USAGE
        PROCESS_MEM_USAGE
        PROCESS_SECURE
        PROCESS_PORT
        LOG_LEVEL
        DATADUMP
        START_TIME
        RESOURCE_TYPES
        PROCESS_ENABLED
        primaryKey {
            PROCESS_NAME
            PROCESS_HOSTNAME
        }
        subTables {
            fields(PROCESS_HOSTNAME, PROCESS_NAME)
                .joiningNewTable(name = "GENESIS_PROCESS_MONITOR", id = 20) {
                    MONITOR_NAME
                    MONITOR_MESSAGE
                    MONITOR_STATE

                    primaryKey(name = "GENESIS_PROCESS_MONITOR_BY_HOSTNAME", id = 1) {
                        PROCESS_HOSTNAME
                        PROCESS_NAME
                        MONITOR_NAME
                    }
                }
        }
    }
    table(name = "SYSTEM_STATUS", id = 13) {
        HOST_NAME
        SYSTEM_CPU_LOAD
        TOTAL_PHYSICAL_MEMORY_SIZE
        FREE_PHYSICAL_MEMORY_SIZE
        primaryKey {
            HOST_NAME
        }
    }
    table(name = "CRON_RULE", id = 14) {
        NAME
        DESCRIPTION
        USER_NAME
        CRON_EXPRESSION
        TIME_ZONE
        RULE_STATUS
        PROCESS_NAME
        MESSAGE_TYPE
        RESULT_EXPRESSION
        primaryKey {
            NAME
        }
    }
    table(name = "CRON_RULE_AUDIT", id = 15) {
        NAME
        DESCRIPTION
        USER_NAME
        CRON_EXPRESSION
        TIME_ZONE
        RULE_STATUS
        PROCESS_NAME
        MESSAGE_TYPE
        RESULT_EXPRESSION
        EVENT_TYPE
        EVENT_DATETIME
        primaryKey {
            NAME
            EVENT_DATETIME
        }
        indices {
            nonUnique {
                EVENT_DATETIME
            }
        }
    }
    table(name = "APPROVAL", id = 16, audit = details(id = 17, sequence = "AA")) {
        sequence(APPROVAL_ID, "AP")
        uuid(APPROVAL_KEY, "AK")
        APPROVAL_STATUS
        APPROVAL_MESSAGE
        USER_NAME
        MESSAGE_TYPE not null
        DESTINATION
        EVENT_MESSAGE not null
        EVENT_DETAILS
        APPROVAL_REQUESTED_AT
        APPROVAL_TYPE
        ADDITIONAL_DETAILS
        ACTIONED_BY
        primaryKey {
            APPROVAL_ID
        }
        indices {
            unique {
                APPROVAL_KEY
            }
        }
    }
    table(name = "APPROVAL_ENTITY", id = 18) {
        APPROVAL_ID
        ENTITY_TABLE
        ENTITY_ID
        APPROVAL_TYPE
        primaryKey(name = "APPROVAL_ENTITY_BY_ID", id = 1) {
            APPROVAL_ID
        }
    }
    table(name = "APPROVAL_ENTITY_COUNTER", id = 19) {
        ENTITY_TABLE
        ENTITY_ID
        AWAITING_COUNTER
        APPROVED_COUNTER
        CANCELLED_COUNTER
        REJECTED_BY_USER_COUNTER
        REJECTED_BY_SERVER_COUNTER
        primaryKey(name = "APPROVAL_ENTITY_COUNTER_BY_ENTITY", id = 1) {
            ENTITY_TABLE
            ENTITY_ID
        }
    }
    table(name = "DATAPIPELINE_OFFSET", id = 27) {
        DATAPIPELINE_NAME
        DEBEZIUM_OFFSET
        primaryKey {
            DATAPIPELINE_NAME
        }
    }
    table(name = "APPLICATION_DNS_ALIAS", id = 28) {
        APPLICATION_ID
        DNS_ALIAS
        primaryKey {
            APPLICATION_ID
        }
        indices {
            unique {
                DNS_ALIAS
            }
        }
    }
    table(name = "DATAPIPELINE_HISTORY", id = 29) {
        DATAPIPELINE_NAME
        DEBEZIUM_HISTORY_RECORD
        primaryKey {
            DATAPIPELINE_NAME
        }
    }
    table(name = "USER", id = 1000, audit = details(1050, "UA")) {
        USER_NAME
        FIRST_NAME
        LAST_NAME
        EMAIL_ADDRESS
        PASSWORD
        PASSWORD_EXPIRY_DATETIME
        LAST_LOGIN
        STATUS
        ONLINE
        COMPANY_NAME
        COMPANY_ID
        DOMAIN
        primaryKey {
            USER_NAME
        }
    }
    val permissionsField = SysDef["ADMIN_PERMISSION_ENTITY_FIELD"]
    table(name = "USER_ATTRIBUTES", id = 1007, audit = details(1052, "AA")) {
        USER_NAME
        USER_TYPE
        ACCESS_TYPE // TODO temporarily to allow for backwards compatibility
        ADDRESS_LINE1
        ADDRESS_LINE2
        ADDRESS_LINE3
        ADDRESS_LINE4
        CITY
        REGION
        POSTAL_CODE
        COUNTRY
        TITLE
        WEBSITE
        MOBILE_NUMBER
        TELEPHONE_NUMBER_DIRECT
        TELEPHONE_NUMBER_OFFICE
        if (permissionsField != null) { // TODO temporarily to allow for backwards compatibility,
            field(permissionsField)
        }
        primaryKey {
            USER_NAME
        }
    }
}
