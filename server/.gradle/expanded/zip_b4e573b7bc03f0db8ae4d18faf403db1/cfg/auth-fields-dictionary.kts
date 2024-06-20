fields {
    field(name = "TAG_VALUE", type = STRING)
    field(name = "USER_LOGIN_AUDIT_ID", type = STRING)
    field(name = "ACTIVE_SESSIONS", type = INT, default = 0)
    field(name = "FAILED_LOGIN_ATTEMPTS", type = INT, default = 0)
    field(name = "REJECTED_LOGIN_ATTEMPTS", type = INT, default = 0)
    field(name = "LAST_LOGIN_DATETIME", type = DATETIME)
    field(name = "AUTH_ACTION", type = STRING)
    field(name = "IP_ADDRESS", type = STRING)
    field(name = "REASON", type = STRING)
    field(name = "PROFILE_NAME", type = STRING)
    field(name = "RIGHT_CODE", type = STRING)
    field(name = "CODE", type = STRING)
    field(name = "HASHED_USER_NAME", type = STRING, sensitive = true, maxSize = 256)
    field(name = "SECRET", type = STRING, sensitive = true, maxSize = 1024)
    field(name = "SSO_TOKEN", type = STRING, sensitive = true, nullable = false)
    field(name = "SSO_SOURCE", type = STRING, nullable = false)
    field(name = "SSO_METHOD", type = ENUM("SAML", "NOT_SET", "OIDC", default = "NOT_SET"))
    field(name = "SSO_DETAILS", type = STRING)
    field(name = "EXPIRES_AT", type = DATETIME, nullable = false)
    field(name = "EXPIRY", type = DATETIME)

    field(name = "PUBLIC_KEY", type = STRING, sensitive = true, maxSize = dbMaxSize)
    field(name = "PUBLIC_KEY_URL", type = STRING, sensitive = true, maxSize = dbMaxSize)
    field(name = "REDIRECT_URL", type = STRING)
    field(name = "KEY_ALGORITHM", type = ENUM("HMAC", "RSA", default = "RSA"))

    field(name = "APP_NAME", type = STRING)
    field(name = "APP_AUTH_URL", type = STRING)
    field(name = "POST_LOGIN_URL", type = STRING)
    field(name = "PROFILE_APP_ID", type = STRING)

    field(name = "ENTITY_VISIBILITY_FIELD", type = STRING)
    field(name = "ENTITY_VISIBILITY_FIELD_REQREP", type = STRING)

    // Fields for LOGIN_ATTEMPTS table
    field(name = "LOGIN_LAST_ATTEMPT_TIME", type = DATETIME, nullable = false)
    field(name = "LOGIN_ATTEMPTS", type = INT, nullable = false)

    // Fields for Notify-Based MFA
    field(name = "MFA_ENABLED", type = BOOLEAN, nullable = false)
    field(name = "MFA_SECRET_TYPE", type = ENUM("TEMPORARY", "ACTIVE", default = "TEMPORARY"))

    field(name = "KV_KEY", type = STRING)
    field(name = "KV_VALUE", type = RAW(Encoding.UTF_8))
}
