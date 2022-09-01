package dev.yc.xmrworker.data.datasource

import dev.yc.xmrworker.data.remote.ApiResult
import dev.yc.xmrworker.data.service.SupportXmrService
import dev.yc.xmrworker.utils.ApiUtil

class SupportXmrDataSourceImpl(
    private val service: SupportXmrService,
) : SupportXmrDataSource {

    override suspend fun fetchIdentifiers(
        address: String?
    ) = ApiUtil.executeAndParse(
        apiScope = {
            service.fetchIdentifiers(address)
        },
        onApiSuccess = { ids ->
            ApiResult.Success(ids)
        }
    )

    override suspend fun fetchMinerDataById(
        address: String?,
        id: String,
    ) = ApiUtil.executeAndParse(
        apiScope = {
            service.fetchEachMinerDataById(address, id)
        },
        onApiSuccess = { stats ->
            ApiResult.Success(stats)
        }
    )
}