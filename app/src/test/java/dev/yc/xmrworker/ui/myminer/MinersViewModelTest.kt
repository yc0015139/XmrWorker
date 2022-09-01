package dev.yc.xmrworker.ui.myminer

import dev.yc.xmrworker.InstantExecutorExtension
import dev.yc.xmrworker.data.ADDRESS
import dev.yc.xmrworker.data.IDENTIFIER
import dev.yc.xmrworker.data.repository.FakeSupportXmrRepository
import dev.yc.xmrworker.ui.myminer.mineritem.MinerState
import dev.yc.xmrworker.ui.myminer.mineritem.MinerUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantExecutorExtension::class)
class MinersViewModelTest {
    private lateinit var viewModel: MinersViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Test
    fun fetchMinerData_uiStateSuccess() = runTest {
        // Arrange
        val testData = MinerUiState.Success(
            minerStates = listOf(
                MinerState(
                    hash = "7000",
                    id = IDENTIFIER,
                    lastTimeInTimestamp = 1657266133,
                    totalHash = "81130718490",
                )
            )
        )
        val excepted = listOf(
            MinerState(
                hash = "7000",
                id = IDENTIFIER,
                lastTimeInTimestamp = 1657266133,
                totalHash = "81130718490",
            )
        )
        setupViewModel(testData)

        // Action
        viewModel.fetchMiners()

        // Assert
        assertEquals(excepted, viewModel.minerData.value)
    }

    @Test
    fun fetchMinerDataWithError_errorEventInvoked() = runTest {
        // Arrange
        val testData = MinerUiState.Error
        val excepted = Unit
        setupViewModel(testData)

        // Action
        viewModel.fetchMiners()

        // Assert
        assertEquals(excepted, viewModel.errorEvent.value?.peekContent())
    }

    @Test
    fun fetchMinerDataWithException_errorEventInvoked() = runTest {
        // Arrange
        val testData = MinerUiState.Exception
        val excepted = Unit
        setupViewModel(testData)

        // Action
        viewModel.fetchMiners()

        // Assert
        assertEquals(excepted, viewModel.errorEvent.value?.peekContent())
    }

    @Test
    fun doRefresh_refreshedEventInvoked() {
        // Arrange
        val testData = MinerUiState.Success(
            minerStates = listOf(
                MinerState(
                    hash = "7000",
                    id = IDENTIFIER,
                    lastTimeInTimestamp = 1657266133,
                    totalHash = "81130718490",
                )
            )
        )
        val excepted = Unit
        setupViewModel(testData)

        // Action
        viewModel.doRefresh()

        // Assert
        assertEquals(excepted, viewModel.refreshedEvent.value?.peekContent())
    }

    @Test
    fun fetchMinerWithSuccessData_emptyLivedataWithFalse() {
        // Arrange
        val testData = MinerUiState.Success(
            minerStates = listOf(
                MinerState(
                    hash = "7000",
                    id = IDENTIFIER,
                    lastTimeInTimestamp = 1657266133,
                    totalHash = "81130718490",
                )
            )
        )
        val excepted = false
        setupViewModel(testData)

        // Action
        viewModel.fetchMiners()

        // Assert
        assertEquals(excepted, viewModel.empty.value)
    }

    @Test
    fun fetchMinerWithEmptyData_emptyLivedataWithTrue() {
        // Arrange
        val testData = MinerUiState.Success(
            listOf()
        )
        val excepted = true
        setupViewModel(testData)

        // Action
        viewModel.fetchMiners()

        // Assert
        assertEquals(excepted, viewModel.empty.value)
    }

    @Test
    fun fetchMinerWithError_emptyLivedataWithTrue() {
        // Arrange
        val testData = MinerUiState.Error
        val excepted = true
        setupViewModel(testData)

        // Action
        viewModel.fetchMiners()

        // Assert
        assertEquals(excepted, viewModel.empty.value)
    }

    @Test
    fun fetchMinerWithException_emptyLivedataWithTrue() {
        // Arrange
        val testData = MinerUiState.Exception
        val excepted = true
        setupViewModel(testData)

        // Action
        viewModel.fetchMiners()

        // Assert
        assertEquals(excepted, viewModel.empty.value)
    }

    private fun setupViewModel(testData: MinerUiState) {
        viewModel = MinersViewModel(
            address = ADDRESS,
            repository = FakeSupportXmrRepository(
                testData = testData,
                dispatcher = testDispatcher,
            ),
            dispatcher = testDispatcher,
        )
    }
}