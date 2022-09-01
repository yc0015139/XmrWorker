package dev.yc.xmrworker.data.repository

import dev.yc.xmrworker.ui.myminer.mineritem.MinerUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FakeSupportXmrRepository(
    private val testData: MinerUiState? = null,
    private val dispatcher: CoroutineDispatcher,
) : SupportXmrRepository {

    override fun fetchMiners(address: String?): Flow<MinerUiState> = flow {
        testData?.let {
            emit(it)
        }
    }.flowOn(dispatcher)
}
