
views {
  view("LOAN_PAYMENTS_GBP", LOAN_TRADE) {
    joins {
      joining(FX_RATE) {
        on(LOAN_TRADE.PAYMENT_CURRENCY to FX_RATE { SOURCE_CURRENCY })
          .and(FX_RATE { TARGET_CURRENCY } to "GBP")
      }
    }

    fields {
      derivedField("PAYMENT_AMOUNT", DOUBLE) {
        withInput(LOAN_TRADE.PAYMENT_AMOUNT, FX_RATE.RATE) { amount, rate ->
          amount * rate
        }
      }
      LOAN_TRADE.CLIENT_NAME
      LOAN_TRADE.PAYMENT_CURRENCY
    }
  }
}
