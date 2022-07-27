package dev.yc.xmrworker.data.repository

import dev.yc.xmrworker.data.datasource.SupportXmrDataSource
import dev.yc.xmrworker.data.remote.ApiResult
import dev.yc.xmrworker.model.MinerStat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SupportXmrRepositoryImpl(
    private val dataSource: SupportXmrDataSource,
    private val dispatcher: CoroutineDispatcher,
) : SupportXmrRepository {

    override fun fetchMiners(address: String?): Flow<List<MinerStat>> {
        return flow {
            when (val apiResultOfIds = dataSource.fetchIdentifiers(address)) {
                is ApiResult.Success -> {
                    val minerStats = fetchMinersByIds(address, apiResultOfIds.result)
                    emit(minerStats)
                }
                is ApiResult.Error, ApiResult.Exception -> emit(listOf()) // TODO: Add error handling
            }
        }.flowOn(dispatcher)
    }

    private suspend fun fetchMinersByIds(
        address: String?,
        ids: List<String>,
    ): MutableList<MinerStat> {
        val miners = mutableListOf<MinerStat>()
        for (id in ids) {
            when (val minerStatResult = dataSource.fetchMinerStatsById(address, id)) {
                is ApiResult.Success -> {
                    val minerStat = minerStatResult.result
                    miners.add(minerStat)
                }
                is ApiResult.Error, ApiResult.Exception -> continue // TODO: Add error handling
            }
        }
        return miners
    }
}