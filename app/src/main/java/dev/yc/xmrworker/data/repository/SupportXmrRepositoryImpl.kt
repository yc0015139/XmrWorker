package dev.yc.xmrworker.data.repository

import dev.yc.xmrworker.data.datasource.SupportXmrDataSource
import dev.yc.xmrworker.data.remote.ApiResult
import dev.yc.xmrworker.ui.myminer.mineritem.MinerState
import dev.yc.xmrworker.ui.myminer.mineritem.MinerUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SupportXmrRepositoryImpl(
    private val dataSource: SupportXmrDataSource,
    private val dispatcher: CoroutineDispatcher,
) : SupportXmrRepository {

    override fun fetchMiners(address: String?): Flow<MinerUiState> {
        return flow {
            when (val apiResultOfIds = dataSource.fetchIdentifiers(address)) {
                is ApiResult.Success -> {
                    val minerUiState = fetchMinersByIds(address, apiResultOfIds.result)
                    emit(minerUiState)
                }
                is ApiResult.Error -> emit(MinerUiState.Error)
                ApiResult.Exception -> emit(MinerUiState.Exception)
            }
        }.flowOn(dispatcher)
    }

    private suspend fun fetchMinersByIds(
        address: String?,
        ids: List<String>,
    ): MinerUiState {
        val miners = mutableListOf<MinerState>()
        for (id in ids) {
            when (val minerDataResult = dataSource.fetchMinerDataById(address, id)) {
                is ApiResult.Success -> {
                    minerDataResult.result.let {
                        miners.add(
                            MinerState(
                                it.hash, it.id, it.lts, it.totalHash
                            )
                        )
                    }

                }
                is ApiResult.Error -> return MinerUiState.Error
                ApiResult.Exception -> return MinerUiState.Exception
            }
        }
        return MinerUiState.Success(miners)
    }
}