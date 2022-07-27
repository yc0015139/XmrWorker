package dev.yc.xmrworker.data.repository

import dev.yc.xmrworker.data.ADDRESS
import dev.yc.xmrworker.data.IDENTIFIER
import dev.yc.xmrworker.data.datasource.FakeSupportXmrDataSource
import dev.yc.xmrworker.model.MinerData
import dev.yc.xmrworker.ui.mineritem.MinerState
import dev.yc.xmrworker.ui.mineritem.MinerUiState
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle

@TestInstance(Lifecycle.PER_CLASS)
class SupportXmrRepositoryImplTest {
    private lateinit var repository: SupportXmrRepository
    private lateinit var testData: Map<String, MinerData>

    @BeforeAll
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
            dispatcher = UnconfinedTestDispatcher(),
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
}