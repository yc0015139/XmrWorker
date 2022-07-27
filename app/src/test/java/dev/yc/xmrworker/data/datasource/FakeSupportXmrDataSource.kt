package dev.yc.xmrworker.data.datasource

import dev.yc.xmrworker.data.remote.ApiResult
import dev.yc.xmrworker.model.MinerData

class FakeSupportXmrDataSource(
    private val testData: Map<String, MinerData>,
) : SupportXmrDataSource {

    override suspend fun fetchIdentifiers(address: String?): ApiResult<List<String>> {
        return ApiResult.Success(testData.keys.toList())
    }

    override suspend fun fetchEachMinerDataById(address: String?, id: String): ApiResult<MinerData> {
        return ApiResult.Success(
            result = testData.getValue(id)
        )
    }
}