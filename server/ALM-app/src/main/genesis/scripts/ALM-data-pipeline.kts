package scripts

/**
 *
 *   System              : ALM
 *   Sub-System          : ALM Configuration
 *   Version             : 1.0
 *   Copyright           : (c) GENESIS
 *   Date                : 2021-09-07
 *
 *   Function : Provide Data Pipeline Configuration for ALM.
 *
 *   Modification History
 *
 */

import global.genesis.db.rx.RxDb.Companion.getValue
import kotlinx.coroutines.flow.toList

pipelines {
    csvSource("alm-cd-import"){
        location="file:"+systemDefinition.getValue("ALM_CSV_UPLOAD_DIR")+"?fileName="+systemDefinition.getValue("ALM_CSV_UPLOAD_CD_FILE")
        map("e2e-test2", CD_TRADE){
            val depositDate = dateValue(name = "DEPOSIT_DATE", format = "yyyy-MM-dd") // H-m-s")
            val maturityDate = dateValue(name = "MATURITY_DATE", format = "yyyy-MM-dd") // H-m-s")
            val maturityAmount = doubleValue(name = "MATURITY_AMOUNT")
            CD_TRADE {
                DEPOSIT_DATE{
                    transform {
                        input.get(depositDate)
                    }}
                MATURITY_DATE{
                    transform {
                        input.get(maturityDate)
                    }}
                CD_ID {
                    property = "CD_ID"
                }
                CLIENT_NAME {
                    property = "CLIENT_NAME"
                }
                DEPOSIT_AMOUNT {
                    property = "DEPOSIT_AMOUNT"
                }
                DEPOSIT_RATE {
                    property = "DEPOSIT_RATE"
                }
                DEPOSIT_CURRENCY {
                    property = "DEPOSIT_CCY"
                }
                MATURITY_AMOUNT {
                    transform {
                        input.get(maturityAmount) * -1.0
                    }}
            }
        }
        onCompletion {
            LOG.info("Successful Rows ==> ${result.successfulRows}")
            LOG.info("Failed Rows ==> ${result.successfulRows}")
            LOG.info("Existing Records ==> ${entityDb.getBulk(CD_TRADE).toList().size}")
            LOG.info("Imported File Name => ${context.fileName}")
        }
    }

}

