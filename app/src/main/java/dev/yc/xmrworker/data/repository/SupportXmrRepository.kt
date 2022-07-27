package dev.yc.xmrworker.data.repository

import dev.yc.xmrworker.ui.mineritem.MinerUiState
import kotlinx.coroutines.flow.Flow

interface SupportXmrRepository {
    fun fetchMiners(address: String?): Flow<List<MinerUiState>>
}