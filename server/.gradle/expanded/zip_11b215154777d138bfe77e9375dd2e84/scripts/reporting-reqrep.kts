import global.genesis.clustersupport.service.ServiceDiscovery
import global.genesis.commons.model.GenesisSet.Companion.genesisSet
import global.genesis.config.system.SystemDefinitionService
import global.genesis.message.core.metadata.MetaDataType

val reportDatasourceList = inject<SystemDefinitionService>().getItem("REPORTING_DATASOURCE_LIST") as? List<String>
val serviceDiscovery = inject<ServiceDiscovery>()

requestReplies {
    requestReply("SAVED_REPORTS", SAVED_REPORTS) {
        request {
            REPORT_NAME
        }
        reply {
            REPORT_NAME
            REPORT_DESCRIPTION
            REPORT_DATASOURCE
            REPORT_COLUMNS
            CREATED_BY
            REPORT_CREATED_ON
        }
    }

    requestReply<GenesisSet, GenesisSet>("ALL_REPORT_DATASOURCES") {
        replyList {
            serviceDiscovery.getResources()
                .filter {
                    it.value.type == MetaDataType.DATASERVER || it.value.type == MetaDataType.REQUEST_SERVER
                }.map {
                    it.key
                }.filter {
                    if (reportDatasourceList.isNullOrEmpty()) {
                        true
                    } else {
                        reportDatasourceList.contains(it)
                    }
                }.map {
                    genesisSet {
                        "DATASOURCE_NAME" with it
                    }
                }.toList()
        }
    }
}
