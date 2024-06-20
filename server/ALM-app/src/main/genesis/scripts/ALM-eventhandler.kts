import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import genesis.global.message.event.LoanMessage
import global.genesis.db.rx.RxDb.Companion.getValue
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

/**
  * This file defines the event handler APIs. These APIs (modeled after CQRS
  * commands) allow callers to manipulate the data in the database. By default,
  * insert, update and delete event handlers (or commands) have been created.
  * These result in the data being written to the database and messages to be
  * published for the rest of the platform to by notified.
  * 
  * Custom event handlers may be added to extend the functionality of the
  * application as well as custom logic added to existing event handlers.
  *
  * The following objects are visible in each eventhandler
  * `event.details` which holds the entity that this event handler is acting upon
  * `entityDb` which is database object used to perform INSERT, MODIFY and UPDATE the records
  * Full documentation on event handler may be found here >> https://learn.genesis.global/docs/server/event-handler/introduction/

 */


@JsonIgnoreProperties(ignoreUnknown = true)
data class LoanResponse(
  val ROWS_COUNT: Int,
  val MESSAGE_TYPE: String,
  val ROW: List<Map<String, Any?>>,
  val MORE_ROWS: Boolean,
  val SOURCE_REF: String,
  val SEQUENCE_ID: Int,
)


eventHandler {
  eventHandler<FxTrade>("FX_TRADE_INSERT", transactional = true) {
    onCommit { event ->
      val details = event.details
      val insertedRow = entityDb.insert(details)
      // return an ack response which contains a list of record IDs
      ack(listOf(mapOf(
        "TRADE_ID" to insertedRow.record.tradeId,
      )))
    }
  }
  eventHandler<FxTrade>("FX_TRADE_MODIFY", transactional = true) {
    onCommit { event ->
      val details = event.details
      //Increment version number and set to amended
      details.tradeVersion = details.tradeVersion + 1
      details.tradeStatus = TradeStatus.Amended
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<FxTrade>("FX_TRADE_DELETE", transactional = true) {
    onCommit { event ->
      val details = event.details
      //Increment version number and set to cancelled
      details.tradeVersion = details.tradeVersion + 1
      details.tradeStatus = TradeStatus.Cancelled
      //Call modify, rather than delete so it stays in blotter
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<Entity>("ENTITY_INSERT", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.insert(details)
      ack()
    }
  }
  eventHandler<Entity>("ENTITY_MODIFY", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<Entity.ByName>("ENTITY_DELETE", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
  eventHandler<Book>("BOOK_INSERT", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.insert(details)
      ack()
    }
  }
  eventHandler<Book>("BOOK_MODIFY", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<Book.ByName>("BOOK_DELETE", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
  eventHandler<Client>("CLIENT_INSERT", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.insert(details)
      ack()
    }
  }
  eventHandler<Client>("CLIENT_MODIFY", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<Client.ByName>("CLIENT_DELETE", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
  eventHandler<Position>("POSITION_INSERT", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.insert(details)
      ack()
    }
  }
  eventHandler<Position>("POSITION_MODIFY", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<Position.ByCurrencySettlementDate>("POSITION_DELETE", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
  eventHandler<LoanTrade>("LOAN_TRADE_INSERT", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.insert(details)
      ack()
    }
  }
  eventHandler<LoanTrade>("LOAN_TRADE_MODIFY", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<LoanTrade.ByLoanId>("LOAN_TRADE_DELETE", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
  eventHandler<CdTrade>("CD_TRADE_INSERT", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.insert(details)
      ack()
    }
  }
  eventHandler<CdTrade>("CD_TRADE_MODIFY", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<CdTrade.ByCdId>("CD_TRADE_DELETE", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }
  eventHandler<FxRate>("FX_RATE_INSERT", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.insert(details)
      ack()
    }
  }
  eventHandler<FxRate>("FX_RATE_MODIFY", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.modify(details)
      ack()
    }
  }
  eventHandler<FxRate.ByTargetCurrencySourceCurrency>("FX_RATE_DELETE", transactional = true) {
    onCommit { event ->
      val details = event.details
      entityDb.delete(details)
      ack()
    }
  }

  eventHandler <LoanMessage> (name="LOAN_MESSAGE") {
    onCommit {
      val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()

      val rawJson = "{\"MESSAGE_TYPE\":\"EVENT_LOGIN_AUTH\",\"SERVICE_NAME\":\"" +
        systemDefinition.getValue("ALM_REST_API_AUTH_PROCESS_NAME").toString() +
        "\",\"DETAILS\":{\"USER_NAME\":\"" + systemDefinition.getValue("ALM_REST_API_USERNAME").toString() +
        "\",\"PASSWORD\":\"" + systemDefinition.getValue("ALM_REST_API_PASSWORD").toString() + "\"}}"
      val requestBody = rawJson.toRequestBody(JSON)

      val client = OkHttpClient().newBuilder().retryOnConnectionFailure(true).build()
      val request = Request.Builder().url(systemDefinition.getValue("ALM_REST_API_URL_LOGIN").toString())
        .post(requestBody)
        .addHeader("SOURCE_REF", "alm-app")
        .addHeader("Connection", "close")
        .build();
      // Send the request synchronously (blocking)
      val response = client.newCall(request).execute()

      if (response.isSuccessful) {
        val responseBody = response.body?.string()
        if (responseBody != null) {
          val objectMapper = ObjectMapper()
          val jsonNode = objectMapper.readTree(responseBody)
          val sessionAuthToken = jsonNode.get("SESSION_AUTH_TOKEN").asText()
          val loanBody = """{}""".toRequestBody(JSON)
          val loanRequest = Request.Builder().url(systemDefinition.getValue("ALM_REST_API_URL_QUERY").toString())
            .post(loanBody)
            .addHeader("SOURCE_REF", "alm-app")
            .addHeader("SESSION_AUTH_TOKEN", sessionAuthToken)
            .addHeader("Connection", "close")
            .build()
          val loanResponse = client.newCall(loanRequest).execute()
          if (loanResponse.isSuccessful) {
            val loanResBody = loanResponse.body?.string()
            if (loanResBody != null) {
              val loanObjectMapper = jacksonObjectMapper()
              loanObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
              val apiResponse: LoanResponse = loanObjectMapper.readValue(loanResBody)
              val loans: List<LoanTrade> = apiResponse.ROW.mapNotNull { row ->
                try {
                  LoanTrade(
                    loanId = (row["LOAN_ID"] as? Int)?.toString()?:"",
                    clientName = (row["CLIENT_NAME"] as? String)?:"",
                    facilityName = (row["FACILITY_NAME"] as? String)?:"",
                    facilityAmount = (row["FACILITY_AMOUNT"] as? Double)?:0.0,
                    facilityCurrency = (row["FACILITY_CCY"] as? String)?:"",
                    drawdownDate = DateTime(row["DRAWDOWN_DATE"] as Long),
                    drawdownAmount = (row["DRAWDOWN_AMOUNT"] as? Double)?:0.0,
                    drawdownCurrency = (row["DRAWDOWN_CURRENCY"] as? String)?:"",
                    paymentDate = DateTime(row["PAYMENT_DATE"] as Long),
                    paymentCurrency = (row["PAYMENT_CURRENCY"] as? String)?:"",
                    paymentAmount = (row["PAYMENT_AMOUNT"] as? Double)?:0.0,
                  )
                } catch (e: Exception) {
                  error("Error mapping loan object ${e.message}")
                }
              }
              for (loan in loans) {
                loan.drawdownAmount = loan.drawdownAmount * -1.0
                entityDb.upsert(loan)
              }
            }
          } else {
            print("Error: $loanResponse.code()")
          }
          loanResponse.close()
        }
      } else {
        println("Error: ${response.code}")
      }
      // Close the response (important to avoid resource leaks)
      response.close()
      ack()
    }
  }

  //TODO - add new or customize event handlers to add validation, access permission checks
  /**
    * If you want to provide some validation before the action, you need to have an onValidate block before the onCommit. The last value of the code block must always be the return message type.
    * eventHandler<THIS_ENTITY>(name = "THIS_ENTITY_INSERT") {
    *      onValidate { event ->
    *          val thisEntity = event.details
    *          require(thisEntity.name != null) { "ThisEntity should have a name" }
    *          ack()
    *      }
    *      onCommit { event ->
    *          ...
    *      }
    *  }
    * You can add permission to the query by using permission codes like below
    * permissioning {
    *     // 'permission Code' list, users must have the permission to access the enclosing resource
    *     permissionCodes = listOf("PERMISSION1", "PERMISSION2")
    * }
    * Full documentation on permissions may be found here https://learn.genesis.global/docs/server/access-control/authorisation-overview/#authorisation
    */
}
