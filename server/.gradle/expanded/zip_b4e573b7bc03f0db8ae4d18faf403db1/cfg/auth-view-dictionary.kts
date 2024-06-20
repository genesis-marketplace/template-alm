views {
    // Below view is for internal use only!! dont use this view in any data-servers/req-reps as it contains password
    view("USER_INSERT_VIEW", USER) {
        joins {
            joining(USER_ATTRIBUTES, backwardsJoin = true) {
                on(USER.USER_NAME to USER_ATTRIBUTES.USER_NAME)
            }
        }

        fields {
            USER {
                USER_NAME
                PASSWORD withPrefix "ONE_TIME"
                LAST_LOGIN
                LAST_NAME
                FIRST_NAME
                COMPANY_NAME
                EMAIL_ADDRESS
                COMPANY_ID
                DOMAIN
            }

            USER_ATTRIBUTES.except(listOf(USER_ATTRIBUTES.USER_NAME, USER_ATTRIBUTES.TIMESTAMP, USER_ATTRIBUTES.RECORD_ID))
        }
    }

    view("USER_AMEND_VIEW", USER) {
        joins {
            joining(USER_ATTRIBUTES, backwardsJoin = true) {
                on(USER.USER_NAME to USER_ATTRIBUTES.USER_NAME)
            }
        }

        fields {
            USER {
                USER_NAME
                LAST_LOGIN
                LAST_NAME
                FIRST_NAME
                COMPANY_NAME
                EMAIL_ADDRESS
                COMPANY_ID
                DOMAIN
            }

            USER_ATTRIBUTES.except(listOf(USER_ATTRIBUTES.USER_NAME, USER_ATTRIBUTES.TIMESTAMP, USER_ATTRIBUTES.RECORD_ID))
        }
    }

    view("PROFILE_RIGHT_VIEW", PROFILE_RIGHT) {
        joins {
            joining(PROFILE, JoinType.INNER) {
                on(PROFILE_RIGHT.PROFILE_NAME to PROFILE.NAME)
            }
            joining(RIGHT, JoinType.INNER) {
                on(PROFILE_RIGHT.RIGHT_CODE to RIGHT.CODE)
            }
        }
        fields {
            PROFILE_RIGHT.PROFILE_NAME
            PROFILE_RIGHT.RIGHT_CODE
        }
    }

    view("PROFILE_USER_VIEW", PROFILE_USER) {
        joins {
            joining(USER, JoinType.INNER, backwardsJoin = true) {
                on(PROFILE_USER.USER_NAME to USER.USER_NAME)
            }
        }

        fields {
            PROFILE_USER.PROFILE_NAME
            PROFILE_USER.USER_NAME
        }
    }

    view("USER_PROFILE_DETAILS_VIEW", PROFILE_USER) {
        joins {
            joining(USER, JoinType.INNER, backwardsJoin = true) {
                on(PROFILE_USER.USER_NAME to USER.USER_NAME)
            }
            joining(PROFILE, JoinType.INNER, backwardsJoin = true) {
                on(PROFILE_USER.PROFILE_NAME to PROFILE.NAME)
            }
        }

        fields {
            USER.FIRST_NAME
            USER.LAST_NAME
            USER.STATUS
            USER.COMPANY_NAME
            USER.EMAIL_ADDRESS
            USER.LAST_LOGIN
            PROFILE.DESCRIPTION
        }
    }

    view("APPROVAL_VIEW", APPROVAL) {
        joins {
            joining(APPROVAL_ENTITY, backwardsJoin = true) {
                on(APPROVAL.APPROVAL_ID to APPROVAL_ENTITY.APPROVAL_ID)
            }
        }

        fields {
            APPROVAL {
                APPROVAL_ID
                USER_NAME
                APPROVAL_STATUS
                APPROVAL_MESSAGE
                MESSAGE_TYPE
                EVENT_DETAILS
                EVENT_MESSAGE
                APPROVAL_REQUESTED_AT
                APPROVAL_TYPE
                ADDITIONAL_DETAILS
                ACTIONED_BY
            }
            APPROVAL_ENTITY {
                ENTITY_TABLE
                ENTITY_ID
                APPROVAL_TYPE withPrefix "ENTITY"
            }
        }
    }

    view("APPROVAL_AUDIT_VIEW", APPROVAL_AUDIT) {
        joins {
            joining(APPROVAL_ENTITY, backwardsJoin = true) {
                on(APPROVAL_AUDIT.APPROVAL_ID to APPROVAL_ENTITY.APPROVAL_ID)
            }
        }

        fields {
            APPROVAL_AUDIT {
                APPROVAL_ID
                USER_NAME
                APPROVAL_STATUS
                APPROVAL_MESSAGE
                MESSAGE_TYPE
                EVENT_DETAILS
                EVENT_MESSAGE
                APPROVAL_REQUESTED_AT
                APPROVAL_TYPE
                ADDITIONAL_DETAILS
                ACTIONED_BY
                AUDIT_EVENT_TYPE
                AUDIT_EVENT_USER
                AUDIT_EVENT_TEXT
                AUDIT_EVENT_DATETIME
            }
            APPROVAL_ENTITY {
                ENTITY_TABLE
                ENTITY_ID
                APPROVAL_TYPE withPrefix "ENTITY"
            }
        }
    }

    view("USER_AUDIT_VIEW", USER_AUDIT) {
        joins {
            joining(USER_ATTRIBUTES_AUDIT, backwardsJoin = true) {
                on(USER_AUDIT.USER_AUDIT_ID to USER_ATTRIBUTES_AUDIT.USER_ATTRIBUTES_AUDIT_ID)
            }
        }

        fields {
            USER_AUDIT {
                USER_AUDIT_ID
                USER_NAME
                LAST_LOGIN
                LAST_NAME
                FIRST_NAME
                ONLINE
                COMPANY_NAME
                STATUS
                EMAIL_ADDRESS
                COMPANY_ID
                AUDIT_EVENT_TYPE
                AUDIT_EVENT_USER
                AUDIT_EVENT_TEXT
                AUDIT_EVENT_DATETIME
            }

            USER_ATTRIBUTES_AUDIT {
                ADDRESS_LINE1 withAlias "ADDRESS_LINE_1"
                ADDRESS_LINE2 withAlias "ADDRESS_LINE_2"
                ADDRESS_LINE3 withAlias "ADDRESS_LINE_3"
                ADDRESS_LINE4 withAlias "ADDRESS_LINE_4"
                CITY
                COUNTRY
                MOBILE_NUMBER
                POSTAL_CODE
                REGION
                TELEPHONE_NUMBER_DIRECT
                TELEPHONE_NUMBER_OFFICE
                TITLE
                USER_TYPE
                WEBSITE
            }
        }
    }
}