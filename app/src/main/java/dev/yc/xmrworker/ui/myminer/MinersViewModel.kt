package dev.yc.xmrworker.ui.myminer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.yc.xmrworker.data.datasource.SupportXmrDataSourceImpl
import dev.yc.xmrworker.data.remote.config.SupportXmrConfig
import dev.yc.xmrworker.data.repository.SupportXmrRepository
import dev.yc.xmrworker.data.repository.SupportXmrRepositoryImpl
import dev.yc.xmrworker.data.service.SupportXmrService
import dev.yc.xmrworker.data.service.generator.ServiceGenerator
import dev.yc.xmrworker.ui.myminer.mineritem.MinerState
import dev.yc.xmrworker.ui.myminer.mineritem.MinerUiState
import dev.yc.xmrworker.utils.livedata.SingleEvent
import dev.yc.xmrworker.utils.livedata.invokeWithPost
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MinersViewModel(
    // TODO: Inject with DI
    private val address: String? = "86mmrEzBQ3RAiiZbEcnJXEZJsuTZE6oSXdwotPxNzEYxZQCfchfNFFYJzGMsii6DLYNxAxW5sGwDBEWvqq9tBsKyBWaKtX1",
    private val repository: SupportXmrRepository = SupportXmrRepositoryImpl(
        dataSource = SupportXmrDataSourceImpl(
            service = ServiceGenerator(
                config = SupportXmrConfig(),
            ).createService(
                serviceClass = SupportXmrService::class.java,
            )
        ),
        dispatcher = Dispatchers.IO,
    ),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {
    private var _minerData = MutableLiveData<List<MinerState>>()
    val minerData: LiveData<List<MinerState>> get() = _minerData

    private var _errorEvent = MutableLiveData<SingleEvent>()
    val errorEvent: LiveData<SingleEvent> get() = _errorEvent

    private val _refreshedEvent = MutableLiveData<SingleEvent>()
    val refreshedEvent: LiveData<SingleEvent> get() = _refreshedEvent

    init {
        fetchMiners()
    }

    fun doRefresh() {
        fetchMiners()
    }

    fun fetchMiners() {
        viewModelScope.launch(dispatcher) {
            repository.fetchMiners(address)
                .map {
                    mapToMinerState(it)
                }
                .collect {
                    _minerData.postValue(it)
                    _refreshedEvent.invokeWithPost()
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
                    _errorEvent.invokeWithPost()
                    break
                }
            }
        }
        return miners
    }

}