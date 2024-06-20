package genesis.cfg
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
fields {
    field(name = "ACTIVE", type = BOOLEAN)
    field(name = "ALERT_ID", type = STRING)
    field(name = "ATTRIBUTES", type = STRING)
    field(name = "AUDIT_CODE", type = STRING)
    field(name = "AUDIT_TEXT", type = STRING, maxSize = dbMaxSize)
    field(name = "CLIENT_ORDER_ID", type = STRING)
    field(name = "RIGHT_CODE", type = STRING)
    field(name = "CREATED_AT", type = DATETIME)
    field(name = "CREATED_BY", type = STRING)
    field(name = "DESCRIPTION", type = STRING, maxSize = dbMaxSize)
    field(name = "ENTITY_ID", type = STRING, maxSize = 255)
    field(name = "EVENT_DATETIME", type = DATETIME)
    field(name = "EVENT_TYPE", type = STRING, maxSize = 200)
    field(name = "EXTERNAL_ID", type = STRING)
    field(name = "FIELD_NAME", type = STRING)
    field(name = "FIELD_VALUE", type = STRING)
    field(name = "HOST", type = STRING)
    field(name = "LAST_ACCESS_TIME", type = DATETIME)
    field(name = "MESSAGE_TYPE", type = STRING)
    field(name = "NAME", type = STRING, maxSize = 255)
    field(name = "NODE", type = STRING)
    field(name = "NOTIFICATION_CODE", type = STRING)
    field(name = "NOTIFICATION_ID", type = STRING)
    field(name = "NOTIFICATION_TIMESTAMP", type = DATETIME)
    field(name = "QUERY_NAME", type = STRING)
    field(name = "REF", type = STRING, maxSize = dbMaxSize)
    field(name = "REJECT_REASON", type = STRING)
    field(name = "RESULT_EXPRESSION", type = STRING, maxSize = dbMaxSize)
    field(name = "RULE_EXPRESSION", type = STRING, maxSize = dbMaxSize)
    field(name = "RULE_STATUS", type = ENUM("ENABLED", "DISABLED", default = "DISABLED"))
    field(name = "RULE_TABLE", type = STRING, maxSize = 255)
    field(name = "RX_SEQUENCE", type = LONG)
    field(name = "SESSION_AUTH_TOKEN", type = STRING, sensitive = true)
    field(name = "SESSION_ID", type = STRING)
    field(name = "SOURCE_ID", type = STRING, maxSize = 200)
    field(name = "RULE_START_TIME", type = STRING)
    field(name = "RULE_END_TIME", type = STRING)
    field(name = "START_TIMESTAMP", type = DATETIME)
    field(name = "SYSTEM_KEY", type = STRING)
    field(name = "SYSTEM_VALUE", type = STRING, sensitive = true, maxSize = dbMaxSize)
    field(name = "TEXT", type = STRING, maxSize = dbMaxSize)
    field(name = "TIMEOUT", type = LONG)
    field(name = "REFRESH_TOKEN_TIMEOUT", type = LONG)
    field(name = "USER_NAME", type = STRING, maxSize = 255)
    field(name = "VERSION", type = INT)
    field(name = "TABLE_OPERATION", type = STRING, default = "INSERT")
    field(name = "PROCESS", type = STRING)
    field(name = "PROCESS_NAME", type = STRING, maxSize = 255)
    field(name = "PROCESS_STATUS", type = BOOLEAN)
    field(name = "PROCESS_STATUS_MESSAGE", type = STRING, maxSize = dbMaxSize)
    field(name = "PROCESS_STATE_TEXT", type = ENUM("DOWN", "ERROR", "WARNING", "STANDBY", "STARTING", "UP", "UNKNOWN", default = "STARTING"))
    field(name = "PROCESS_RESOURCES", type = STRING, maxSize = dbMaxSize)
    field(name = "PROCESS_HOSTNAME", type = STRING, maxSize = 255)
    field(name = "PROCESS_CPU_USAGE", type = DOUBLE)
    field(name = "PROCESS_MEM_USAGE", type = DOUBLE)
    field(name = "PROCESS_SECURE", type = BOOLEAN)
    field(name = "PROCESS_PORT", type = INT)
    field(name = "PROCESS_ENABLED", type = BOOLEAN, default = true)
    field(name = "RESOURCE_TYPES", type = STRING, maxSize = dbMaxSize)
    field(name = "LOG_LEVEL", type = STRING)
    field(name = "DATADUMP", type = BOOLEAN)
    field(name = "START_TIME", type = DATETIME)
    field(name = "HOST_NAME", type = STRING, maxSize = 255)
    field(name = "SYSTEM_CPU_LOAD", type = DOUBLE)
    field(name = "TOTAL_PHYSICAL_MEMORY_SIZE", type = LONG)
    field(name = "FREE_PHYSICAL_MEMORY_SIZE", type = LONG)
    field(name = "CRON_EXPRESSION", type = STRING, maxSize = 255)
    field(name = "TIME_ZONE", type = STRING)
    field(name = "APPROVAL_ID", type = STRING)
    field(name = "APPROVAL_KEY", type = STRING)
    field(
        name = "APPROVAL_STATUS",
        type = ENUM("PENDING", "APPROVED", "CANCELLED", "REJECTED_BY_USER", "REJECTED_BY_SERVICE", default = "PENDING")
    )
    field(name = "APPROVAL_MESSAGE", type = STRING, maxSize = dbMaxSize)
    field(name = "DESTINATION", type = STRING)
    field(name = "EVENT_MESSAGE", type = STRING, maxSize = dbMaxSize)
    field(name = "EVENT_DETAILS", type = STRING, maxSize = dbMaxSize)
    field(name = "ENTITY_TABLE", type = STRING)
    field(name = "AWAITING_COUNTER", type = INT)
    field(name = "APPROVED_COUNTER", type = INT)
    field(name = "CANCELLED_COUNTER", type = INT)
    field(name = "REJECTED_BY_USER_COUNTER", type = INT)
    field(name = "REJECTED_BY_SERVER_COUNTER", type = INT)
    field(name = "MONITOR_NAME", type = STRING, maxSize = 200)
    field(name = "MONITOR_MESSAGE", type = STRING, maxSize = dbMaxSize)
    field(name = "MONITOR_STATE", type = STRING)
    field(name = "DATAPIPELINE_NAME", type = STRING)
    field(name = "DEBEZIUM_OFFSET", type = RAW(encoding = Encoding.BASE_64))
    field(name = "DEBEZIUM_HISTORY_RECORD", type = RAW(encoding = Encoding.BASE_64))
    field(name = "APPLICATION_ID", type = STRING)
    field(name = "DNS_ALIAS", type = STRING)
    // From auth-server - user and user attributes fields
    field(name = "FIRST_NAME", type = STRING)
    field(name = "LAST_NAME", type = STRING)
    field(name = "EMAIL_ADDRESS", type = STRING)
    field(name = "PASSWORD", type = STRING, maxSize = 255, sensitive = true)
    field(name = "PASSWORD_EXPIRY_DATETIME", type = DATETIME)
    field(name = "REFRESH_TOKEN", type = STRING, sensitive = true)
    field(name = "LAST_LOGIN", type = DATE)
    field(name = "STATUS", type = STRING)
    field(name = "ONLINE", type = BOOLEAN, default = false)
    field(name = "COMPANY_NAME", type = STRING)
    field(name = "COMPANY_ID", type = STRING)
    field(name = "DOMAIN", type = STRING)
    field(name = "USER_TYPE", type = ENUM("USER", "ADVISOR", "CLIENT", "SYSTEM", "API", default = "USER"))
    field(name = "ACCESS_TYPE", type = ENUM("ALL", "ENTITY", "MULTI_ENTITY", default = "ALL"))
    field(name = "ADDRESS_LINE1", type = STRING, maxSize = 400)
    field(name = "ADDRESS_LINE2", type = STRING, maxSize = 400)
    field(name = "ADDRESS_LINE3", type = STRING, maxSize = 400)
    field(name = "ADDRESS_LINE4", type = STRING, maxSize = 400)
    field(name = "CITY", type = STRING)
    field(name = "REGION", type = STRING)
    field(name = "POSTAL_CODE", type = STRING)
    field(name = "COUNTRY", type = STRING)
    field(name = "TITLE", type = STRING)
    field(name = "WEBSITE", type = STRING)
    field(name = "MOBILE_NUMBER", type = STRING)
    field(name = "TELEPHONE_NUMBER_DIRECT", type = STRING)
    field(name = "TELEPHONE_NUMBER_OFFICE", type = STRING)
    field(name = "APPROVAL_REQUESTED_AT", type = DATETIME)
    field(name = "APPROVAL_TYPE", type = ENUM("NEW", "UPDATE", "REMOVE", "UNKNOWN", default = "UNKNOWN"))
    field(name = "ADDITIONAL_DETAILS", type = STRING, maxSize = 4096)
    field(name = "ACTIONED_BY", type = STRING)
    field(name = "IS_TEMPLATE", type = BOOLEAN, default = false)
    field(name = "RULE_EXECUTION_STRATEGY", type = ENUM("UNLIMITED", "ONCE_ONLY", "ONCE_PER_RECORD", default = "UNLIMITED"))
    field(name = "RULE_TYPE", type = STRING)
    field(name = "PARAM_SOURCE_TYPE", type = ENUM("USER_TEXT", "DEFINED_GROUP", "REQ_REP", default = "USER_TEXT"))
    field(name = "PARAM_SOURCE", type = STRING, default = "")
    field(name = "PARAM_TYPE", type = ENUM("FIELD", "STRING", "BOOLEAN", "NUMBER", default = "FIELD"))
    field(name = "PARAM_NAME", type = STRING)
    field(name = "PARAM_LABEL", type = STRING)
    field(name = "PARAM_OPERATOR", type = STRING)
    field(name = "DYNAMIC_RULE_ID", type = STRING)
    field(name = "RECORD_ID_REF", type = LONG)
}