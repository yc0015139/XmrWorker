package dev.yc.xmrworker.data.repository

import dev.yc.xmrworker.data.datasource.SupportXmrDataSource
import dev.yc.xmrworker.data.remote.ApiResult
import dev.yc.xmrworker.ui.mineritem.MinerState
import dev.yc.xmrworker.ui.mineritem.MinerUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SupportXmrRepositoryImpl(
    private val dataSource: SupportXmrDataSource,
    private val dispatcher: CoroutineDispatcher,
) : SupportXmrRepository {

    override fun fetchMiners(address: String?): Flow<List<MinerUiState>> {
        return flow {
            when (val apiResultOfIds = dataSource.fetchIdentifiers(address)) {
                is ApiResult.Success -> {
                    val minerData = fetchMinersByIds(address, apiResultOfIds.result)
                    emit(minerData)
                }
                is ApiResult.Error -> emit(listOf(MinerUiState.Error))
                ApiResult.Exception -> emit(listOf(MinerUiState.Exception))
            }
        }.flowOn(dispatcher)
    }

    private suspend fun fetchMinersByIds(
        address: String?,
        ids: List<String>,
    ): MutableList<MinerUiState> {
        val miners = mutableListOf<MinerUiState>()
        for (id in ids) {
            when (val minerDataResult = dataSource.fetchEachMinerDataById(address, id)) {
                is ApiResult.Success -> {
                    val minerData = minerDataResult.result
                    miners.add(
                        MinerUiState.Success(
                            minerState = minerData.let {
                                MinerState(it.hash, it.id, it.lts, it.totalHash)
                            }
                        )
                    )
                }
                is ApiResult.Error -> miners.add(MinerUiState.Error)
                ApiResult.Exception -> miners.add(MinerUiState.Exception)
            }
        }
        return miners
    }
}