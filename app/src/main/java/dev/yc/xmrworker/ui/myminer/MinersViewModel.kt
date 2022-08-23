package dev.yc.xmrworker.ui.myminer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.yc.xmrworker.data.repository.SupportXmrRepository
import dev.yc.xmrworker.ui.myminer.mineritem.MinerState
import dev.yc.xmrworker.ui.myminer.mineritem.MinerUiState
import dev.yc.xmrworker.utils.livedata.SingleEvent
import dev.yc.xmrworker.utils.livedata.invoke
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MinersViewModel(
    private val address: String? = null,
    private val repository: SupportXmrRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private var _minerData = MutableLiveData<List<MinerState>>()
    val minerData: LiveData<List<MinerState>> get() = _minerData

    private var _error = MutableLiveData<SingleEvent>()
    val error: LiveData<SingleEvent> get() = _error

    fun fetchMiners() {
        viewModelScope.launch(dispatcher) {
            repository.fetchMiners(address)
                .map {
                    mapToMinerState(it)
                }
                .collect {
                    _minerData.value = it
                }
        }
    }

    private fun mapToMinerState(uiStates: List<MinerUiState>): List<MinerState> {
        val miners = mutableListOf<MinerState>()
        for (uiState in uiStates) {
            when (uiState) {
                is MinerUiState.Success -> miners.add(uiState.minerState)
                else -> {
                    miners.clear()
                    _error.invoke()
                    break
                }
            }
        }
        return miners
    }

}