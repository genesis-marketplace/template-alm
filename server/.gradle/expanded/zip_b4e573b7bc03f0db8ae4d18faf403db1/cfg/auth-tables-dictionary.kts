tables {
    table(name = "KEY_VALUE", id = 1014) {
        USER_NAME
        KV_KEY
        KV_VALUE
        primaryKey {
            USER_NAME
            KV_KEY
        }
    }

    table(name = "PROFILE", id = 1002, audit = details(1053, "PR")) {
        NAME
        DESCRIPTION
        STATUS
        primaryKey {
            NAME
        }
    }

    table(name = "PROFILE_USER", id = 1003, audit = details(1051, "PA")) {
        PROFILE_NAME
        USER_NAME
        primaryKey {
            PROFILE_NAME
            USER_NAME
        }
        indices {
            nonUnique {
                USER_NAME
                PROFILE_NAME
            }
        }
    }

    table(name = "PROFILE_APP", id = 1025, audit = details(1055, sequence = "PA")) {
        sequence(PROFILE_APP_ID, "PA")
        PROFILE_NAME
        APP_NAME
        primaryKey {
            PROFILE_APP_ID
        }
    }

    table(name = "RIGHT", id = 1004) {
        CODE
        DESCRIPTION
        primaryKey {
            CODE
        }
    }

    table(name = "PROFILE_RIGHT", id = 1005, audit = details(1054, "RP")) {
        RIGHT_CODE
        PROFILE_NAME
        primaryKey {
            RIGHT_CODE
            PROFILE_NAME
        }
        indices {
            unique {
                PROFILE_NAME
                RIGHT_CODE
            }
        }
    }

    table(name = "TAG", id = 1006) {
        CODE
        TAG_VALUE
        ENTITY_ID
        primaryKey {
            CODE
            ENTITY_ID
        }
        indices {
            nonUnique {
                ENTITY_ID
                CODE
            }
        }
    }
    val permissionsField = SysDef["ADMIN_PERMISSION_ENTITY_FIELD"]

    table(name = "USER_LOGIN", id = 1008) {
        USER_NAME
        ACTIVE_SESSIONS
        FAILED_LOGIN_ATTEMPTS
        REJECTED_LOGIN_ATTEMPTS
        LAST_LOGIN_DATETIME
        primaryKey {
            USER_NAME
        }
    }
    table(name = "USER_LOGIN_AUDIT", id = 1009) {
        sequence(USER_LOGIN_AUDIT_ID, "UL")
        USER_NAME
        AUTH_ACTION
        IP_ADDRESS
        REASON
        primaryKey {
            USER_LOGIN_AUDIT_ID
        }
        indices {
            nonUnique {
                USER_NAME
            }
        }
    }
    table(name = "USER_PASSWORD_HISTORY", id = 1010) {
        USER_NAME
        PASSWORD
        primaryKey {
            USER_NAME
            TIMESTAMP
        }
    }
    table(name = "MFA_SECRET", id = 1011) {
        HASHED_USER_NAME // HashKey of USER_NAME+SALT, acts as key for lookup of user secret
        SECRET // Encrypted - Decrypted on lookup
        primaryKey {
            HASHED_USER_NAME
        }
    }

    table(name = "JWT_CONFIG", id = 1013) {
        DOMAIN not null
        PUBLIC_KEY
        PUBLIC_KEY_URL
        REDIRECT_URL not null
        KEY_ALGORITHM

        primaryKey {
            DOMAIN
        }
    }

    table("SSO_TOKEN", 1015) {
        SSO_TOKEN
        USER_NAME not null
        EXPIRES_AT
        HOST_NAME not null

        primaryKey {
            Fields.SSO_TOKEN
        }
    }

    table("SSO_USER", 1016) {
        USER_NAME
        SSO_SOURCE
        SSO_METHOD
        SSO_DETAILS

        primaryKey {
            Fields.USER_NAME
        }

        indices {
            nonUnique("SSO_USER_BY_SOURCE_AND_METHOD") {
                SSO_SOURCE
                SSO_METHOD
            }
        }
    }

    table("APPLICATION", 1017) {
        APP_NAME
        primaryKey {
            APP_NAME
        }
    }

    table("NOTIFY_BASED_MFA_STATUS", 1022, audit = details(1023, "NM")) {
        USER_NAME
        MFA_ENABLED
        primaryKey {
            USER_NAME
        }
    }

    table(name = "NOTIFY_BASED_MFA_SECRET", 1024, audit = details(1026, "NS")) {
        USER_NAME
        HOST_NAME
        SECRET
        MFA_SECRET_TYPE
        EXPIRY not null
        primaryKey {
            USER_NAME
            HOST_NAME
        }
    }

    // BELOW THIS POINT IS SPECIFIC TO AUTH PERMS, IT WILL BE EXTRACTED AT SOME POINT
    /*val permissionsField = SysDef["ADMIN_PERMISSION_ENTITY_FIELD"]
    if (permissionsField != null) {
        table(name = "APP_USER_ATTRIBUTES", id = 1007, audit = details(1052, "AA")) {
            USER_NAME
            ACCESS_TYPE
            Fields[permissionsField]
            primaryKey {
                USER_NAME
            }
        }
    }*/

    table("ENTITY_VISIBILITY_FIELDS", 1018) {
        ENTITY_VISIBILITY_FIELD
        ENTITY_VISIBILITY_FIELD_REQREP
        primaryKey {
            ENTITY_VISIBILITY_FIELD
        }
    }

    table("PASSWORD_RESET", 1019, audit = details(1020, "RE")) {
        USER_NAME
        PASSWORD not null
        EXPIRY not null
        primaryKey {
            USER_NAME
        }
    }

    table("USER_LOGIN_ATTEMPT", 1021) {
        USER_NAME
        LOGIN_LAST_ATTEMPT_TIME not null
        LOGIN_ATTEMPTS not null
        primaryKey {
            USER_NAME
        }
    }

    val permissionsTable = SysDef["ADMIN_PERMISSION_ENTITY_TABLE"]

    if (permissionsTable != null && permissionsField != null) {
        val multiEntity = SysDef["ADMIN_PERMISSION_MULTI_ENTITY_FIELD"]

        table(name = "USER_${permissionsTable}_MAP", id = 1012) {
            USER_NAME
            field(permissionsField)
            if (multiEntity != null) field(multiEntity)
            val primaryKeyFields =
                if (multiEntity != null) arrayOf(permissionsField)
                else emptyArray()
            primaryKey(USER_NAME.name, *primaryKeyFields)

            indices {
                val nonUniqueFields =
                    if (multiEntity != null) arrayOf(multiEntity)
                    else emptyArray()

                nonUnique(permissionsField, *nonUniqueFields)
                    .name("USER_${permissionsTable}_MAP_BY_${permissionsField}")
            }
        }
    }
}
