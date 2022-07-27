package dev.yc.xmrworker.data.datasource

import dev.yc.xmrworker.data.remote.ApiResult
import dev.yc.xmrworker.model.MinerStat

class FakeSupportXmrDataSource(
    private val testData: Map<String, MinerStat>,
) : SupportXmrDataSource {

    override suspend fun fetchIdentifiers(address: String?): ApiResult<List<String>> {
        return ApiResult.Success(testData.keys.toList())
    }

    override suspend fun fetchMinerStatsById(address: String?, id: String): ApiResult<MinerStat> {
        return ApiResult.Success(
            result = testData.getValue(id)
        )
    }
}