package dev.yc.xmrworker.ui.myminer.mineritem

sealed interface MinerUiState {
    data class Success(val minerStates: List<MinerState>) : MinerUiState
    object Error : MinerUiState
    object Exception : MinerUiState
}