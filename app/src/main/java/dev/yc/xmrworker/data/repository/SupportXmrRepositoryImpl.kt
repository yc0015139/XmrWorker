package dev.yc.xmrworker.data.repository

import dev.yc.xmrworker.data.datasource.SupportXmrDataSource
import dev.yc.xmrworker.data.remote.ApiResult
import dev.yc.xmrworker.model.MinerData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SupportXmrRepositoryImpl(
    private val dataSource: SupportXmrDataSource,
    private val dispatcher: CoroutineDispatcher,
) : SupportXmrRepository {

    override fun fetchMiners(address: String?): Flow<List<MinerData>> {
        return flow {
            when (val apiResultOfIds = dataSource.fetchIdentifiers(address)) {
                is ApiResult.Success -> {
                    val minerData = fetchMinersByIds(address, apiResultOfIds.result)
                    emit(minerData)
                }
                is ApiResult.Error, ApiResult.Exception -> emit(listOf()) // TODO: Add error handling
            }
        }.flowOn(dispatcher)
    }

    private suspend fun fetchMinersByIds(
        address: String?,
        ids: List<String>,
    ): MutableList<MinerData> {
        val miners = mutableListOf<MinerData>()
        for (id in ids) {
            when (val minerDataResult = dataSource.fetchEachMinerDataById(address, id)) {
                is ApiResult.Success -> {
                    val minerData = minerDataResult.result
                    miners.add(minerData)
                }
                is ApiResult.Error, ApiResult.Exception -> continue // TODO: Add error handling
            }
        }
        return miners
    }
}