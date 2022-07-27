package dev.yc.xmrworker.data.datasource

import dev.yc.xmrworker.data.remote.ApiResult
import dev.yc.xmrworker.model.MinerData

interface SupportXmrDataSource {

    suspend fun fetchIdentifiers(
        address: String?,
    ): ApiResult<List<String>>

    suspend fun fetchEachMinerDataById(
        address: String?,
        id: String,
    ): ApiResult<MinerData>
}