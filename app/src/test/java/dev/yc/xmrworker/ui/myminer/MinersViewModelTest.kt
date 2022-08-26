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
    private lateinit var testData: List<MinerUiState>
    private val testDispatcher = UnconfinedTestDispatcher()

    @Test
    fun fetchMinerData_uiStateSuccess() = runTest {
        // Arrange
        testData = listOf(
            MinerUiState.Success(
                minerState = MinerState(
                    hash = "7000",
                    id = IDENTIFIER,
                    lastTimeInTimestamp = 1657266133,
                    totalHash = "81130718490",
                )
            )
        )
        viewModel = MinersViewModel(
            address = ADDRESS,
            repository = FakeSupportXmrRepository(
                testData = testData,
                dispatcher = testDispatcher,
            ),
            dispatcher = testDispatcher,
        )
        val excepted = listOf(
            MinerState(
                hash = "7000",
                id = IDENTIFIER,
                lastTimeInTimestamp = 1657266133,
                totalHash = "81130718490",
            )
        )

        // Action
        viewModel.fetchMiners()

        // Assert
        assertEquals(excepted, viewModel.minerData.value)
    }

    @Test
    fun fetchMinerDataWithError_errorEventInvoked() = runTest {
        // Arrange
        testData = listOf(
            MinerUiState.Error
        )
        viewModel = MinersViewModel(
            address = ADDRESS,
            repository = FakeSupportXmrRepository(
                testData = testData,
                dispatcher = testDispatcher,
            ),
            dispatcher = testDispatcher,
        )
        val excepted = Unit

        // Action
        viewModel.fetchMiners()

        // Assert
        assertEquals(excepted, viewModel.errorEvent.value?.peekContent())
    }

    @Test
    fun fetchMinerDataWithException_errorEventInvoked() = runTest {
        // Arrange
        testData = listOf(
            MinerUiState.Exception
        )
        viewModel = MinersViewModel(
            address = ADDRESS,
            repository = FakeSupportXmrRepository(
                testData = testData,
                dispatcher = testDispatcher,
            ),
            dispatcher = testDispatcher,
        )
        val excepted = Unit

        // Action
        viewModel.fetchMiners()

        // Assert
        assertEquals(excepted, viewModel.errorEvent.value?.peekContent())
    }

    @Test
    fun doRefresh_refreshedEventInvoked() {
        // Arrange
        testData = listOf(
            MinerUiState.Success(
                minerState = MinerState(
                    hash = "7000",
                    id = IDENTIFIER,
                    lastTimeInTimestamp = 1657266133,
                    totalHash = "81130718490",
                )
            )
        )
        viewModel = MinersViewModel(
            address = ADDRESS,
            repository = FakeSupportXmrRepository(
                testData = testData,
                dispatcher = testDispatcher,
            ),
            dispatcher = testDispatcher,
        )
        val excepted = Unit

        // Action
        viewModel.doRefresh()

        // Assert
        assertEquals(excepted, viewModel.refreshedEvent.value?.peekContent())
    }
}