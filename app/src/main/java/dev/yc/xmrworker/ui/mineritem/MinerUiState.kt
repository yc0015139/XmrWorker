package dev.yc.xmrworker.ui.mineritem

sealed interface MinerUiState {
    data class Success(val minerState: MinerState) : MinerUiState
    object Error : MinerUiState
    object Exception : MinerUiState
}