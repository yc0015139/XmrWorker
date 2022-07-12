package dev.yc.xmrworker.data.datasource

import dev.yc.xmrworker.data.ADDRESS
import dev.yc.xmrworker.data.GLOBAL_ID
import dev.yc.xmrworker.data.IDENTIFIER
import dev.yc.xmrworker.data.SUPPORT_XMR_CONFIG
import dev.yc.xmrworker.data.remote.ApiResult
import dev.yc.xmrworker.data.service.SupportXmrService
import dev.yc.xmrworker.data.service.generator.ServiceGenerator
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.api.fail

// TODO: Add test cases with support xmr data source
@TestInstance(Lifecycle.PER_CLASS)
class SupportXmrDataSourceTest {
    private lateinit var dataSource: SupportXmrDataSource

    // TODO: Implement mocked data source
    // TODO: Add test cases for network unavailable
    @BeforeAll
    fun setupDataSource() {
        dataSource = SupportXmrDataSourceImpl(
            service = ServiceGenerator(
                config = SUPPORT_XMR_CONFIG,
            ).createService(
                serviceClass = SupportXmrService::class.java,
            )
        )
    }

    @Test
    fun fetchIds_sizeOfIdentifiersIsNotZeroOnApiSuccess() = runTest {
        val apiResult = dataSource.fetchIdentifiers(ADDRESS)

        val result = when (apiResult) {
            is ApiResult.Success -> apiResult.result
            else -> {
                fail("Something wrong on network connection.")
            }
        }
        val sizeOfIds = result.size
        assertNotEquals(0, sizeOfIds)
    }

    @Test
    fun fetchMinerStats_idOfMinerIsNotGlobalIdOnApiSuccess() = runTest {
        val apiResult = dataSource.fetchMinerStatsById(
            address = ADDRESS,
            id = IDENTIFIER,
        )

        val minerStat = when (apiResult) {
            is ApiResult.Success -> {
                apiResult.result
            }
            else -> {
                fail("Something wrong on network connection.")
            }
        }
        assertNotEquals(GLOBAL_ID, minerStat.id)
    }
}