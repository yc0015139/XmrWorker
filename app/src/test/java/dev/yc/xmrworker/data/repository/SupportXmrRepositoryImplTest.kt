package dev.yc.xmrworker.data.repository

import dev.yc.xmrworker.data.ADDRESS
import dev.yc.xmrworker.data.IDENTIFIER
import dev.yc.xmrworker.data.datasource.FakeSupportXmrDataSource
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
        repository = SupportXmrRepositoryImpl(
            dataSource = FakeSupportXmrDataSource(
                testIds = listOf(IDENTIFIER)
            ),
            dispatcher = UnconfinedTestDispatcher()
        )
    }

    @Test
    fun fetchMiners_minersNotNull() = runTest {
        // Act
        val flow = repository.fetchMiners(ADDRESS)

        flow.collect {
            // Assert
            assertNotNull(it)
        }
    }
}