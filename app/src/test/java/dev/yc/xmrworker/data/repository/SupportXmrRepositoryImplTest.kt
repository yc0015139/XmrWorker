package dev.yc.xmrworker.data.repository

import dev.yc.xmrworker.data.ADDRESS
import dev.yc.xmrworker.data.IDENTIFIER
import dev.yc.xmrworker.data.datasource.FakeSupportXmrDataSource
import dev.yc.xmrworker.data.remote.ApiResult
import dev.yc.xmrworker.model.MinerData
import dev.yc.xmrworker.ui.mineritem.MinerState
import dev.yc.xmrworker.ui.mineritem.MinerUiState
import dev.yc.xmrworker.utils.ApiUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle

@TestInstance(Lifecycle.PER_CLASS)
class SupportXmrRepositoryImplTest {
    private lateinit var repository: SupportXmrRepository
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private lateinit var testData: Map<String, MinerData>

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        // Arrange
        testData = mapOf(
            IDENTIFIER to MinerData(
                hash = "7000",
                id = IDENTIFIER,
                invalidShares = 5,
                lts = 1657266133,
                totalHash = "81130718490",
                validShares = 269930,
            )
        )

        repository = SupportXmrRepositoryImpl(
            dataSource = FakeSupportXmrDataSource(testData),
            dispatcher = testDispatcher,
        )
    }

    @Test
    fun fetchMiners_minersAreNotNull() = runTest {
        // Act
        val flow = repository.fetchMiners(ADDRESS)

        flow.collect { actual ->
            // Assert
            assertNotNull(actual)
        }
    }


    @Test
    fun fetchMiners_minersAreCurrent() = runTest {
        // Arrange
        val expected = listOf(
            MinerUiState.Success(
                minerState = testData.getValue(IDENTIFIER).run {
                    MinerState(
                        hash = hash,
                        id = id,
                        lastTimeInTimestamp = lts,
                        totalHash = totalHash,
                    )
                }
            )
        )

        // Act
        val flow = repository.fetchMiners(ADDRESS)

        flow.collect { actual ->
            // Assert
            assertEquals(expected, actual)
        }
    }

    @Test
    fun fetchMiners_minerFetchingWithErrorInBadRequest() = runTest {
        // Arrange
        repository = SupportXmrRepositoryImpl(
            dataSource = FakeSupportXmrDataSource(
                apiResult = ApiResult.Error(ApiUtil.BAD_REQUEST)
            ),
            dispatcher = testDispatcher
        )
        val expected = listOf(
            MinerUiState.Error
        )

        // Action
        val flow = repository.fetchMiners(ADDRESS)

        // Assert
        flow.collect { actual ->
            assertEquals(expected, actual)
        }
    }

    @Test
    fun fetchMiners_minerFetchingWithException() = runTest {
        // Arrange
        repository = SupportXmrRepositoryImpl(
            dataSource = FakeSupportXmrDataSource(
                apiResult = ApiResult.Exception
            ),
            dispatcher = testDispatcher
        )
        val expected = listOf(
            MinerUiState.Exception
        )

        // Action
        val flow = repository.fetchMiners(ADDRESS)

        // Assert
        flow.collect { actual ->
            assertEquals(expected, actual)
        }
    }
}