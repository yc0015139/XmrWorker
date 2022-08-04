package dev.yc.xmrworker.data.datasource

import dev.yc.xmrworker.data.remote.ApiResult
import dev.yc.xmrworker.model.MinerData

class FakeSupportXmrDataSource(
    private val testData: Map<String, MinerData> = mapOf(),
    private val apiResult: ApiResult<Any> = ApiResult.Success(Any()),
) : SupportXmrDataSource {

    override suspend fun fetchIdentifiers(address: String?): ApiResult<List<String>> {
        return when (apiResult) {
            is ApiResult.Success -> ApiResult.Success(testData.keys.toList())
            else -> apiResult as ApiResult<List<String>>
        }
    }

    override suspend fun fetchEachMinerDataById(
        address: String?,
        id: String,
    ): ApiResult<MinerData> {
        return ApiResult.Success(
            result = testData.getValue(id)
        )
    }
}