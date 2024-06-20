views {
    view("USER_VIEW", USER) {
        joins {
            joining(USER_ATTRIBUTES, backwardsJoin = true) {
                on(USER.USER_NAME to USER_ATTRIBUTES.USER_NAME)
            }
        }

        fields {
            USER.except(
                listOf(
                    USER.RECORD_ID,
                    USER.TIMESTAMP,
                    USER.PASSWORD,
                    USER.PASSWORD_EXPIRY_DATETIME
                )
            )

            USER_ATTRIBUTES.except(
                listOf(
                    USER_ATTRIBUTES.USER_NAME,
                    USER_ATTRIBUTES.TIMESTAMP,
                    USER_ATTRIBUTES.RECORD_ID
                )
            )
        }
    }
}
