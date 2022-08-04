package dev.yc.xmrworker.ui.myminer.mineritem

data class MinerState(
    val hash: String,
    val id: String,
    val lastTimeInTimestamp: Int,
    val totalHash: String,
)
