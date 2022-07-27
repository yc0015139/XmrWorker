package dev.yc.xmrworker.data.repository

import dev.yc.xmrworker.data.ADDRESS
import dev.yc.xmrworker.data.IDENTIFIER
import dev.yc.xmrworker.data.datasource.FakeSupportXmrDataSource
import dev.yc.xmrworker.model.MinerData
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle

@TestInstance(Lifecycle.PER_CLASS)
class SupportXmrRepositoryImplTest {
    private lateinit var repository: SupportXmrRepository

    @BeforeAll
    fun setup() {
        // Arrange
        val testData = mapOf(
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
    fun fetchMiners_minersNotNull() = runTest {
        // Act
        val flow = repository.fetchMiners(ADDRESS)

        flow.collect { actual ->
            // Assert
            assertNotNull(actual)
        }
    }
}