package dev.yc.xmrworker.data.repository

import dev.yc.xmrworker.model.MinerStat
import kotlinx.coroutines.flow.Flow

interface SupportXmrRepository {
    fun fetchMiners(address: String?): Flow<List<MinerStat>>
}