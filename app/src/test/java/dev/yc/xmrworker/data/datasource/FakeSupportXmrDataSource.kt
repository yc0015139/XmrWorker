package dev.yc.xmrworker.data.datasource

import dev.yc.xmrworker.data.remote.ApiResult
import dev.yc.xmrworker.model.MinerStat

class FakeSupportXmrDataSource(private val testIds: List<String>) : SupportXmrDataSource {
    override suspend fun fetchIdentifiers(address: String?): ApiResult<List<String>> {
        return ApiResult.Success(testIds)
    }

    override suspend fun fetchMinerStatsById(address: String?, id: String): ApiResult<MinerStat> {
        return ApiResult.Success(
            result = MinerStat(
                hash = "7000",
                id = testIds.first(),
                invalidShares = 5,
                lts = 1657266133,
                totalHash = "81130718490",
                validShares = 269930,
            )
        )
    }
}