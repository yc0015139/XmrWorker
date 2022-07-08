package dev.yc.xmrworker.data.datasource

import dev.yc.xmrworker.data.remote.ApiResult
import dev.yc.xmrworker.data.remote.config.SupportXmrConfig
import dev.yc.xmrworker.data.service.SupportXmrService
import dev.yc.xmrworker.data.service.generator.ServiceGenerator
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

// TODO: Add test cases with support xmr data source
class SupportXmrDataSourceTest {
    private lateinit var dataSource: SupportXmrDataSource

    // TODO: Implement mocked data source
    // TODO: Add test cases for network unavailable
    @Before
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
                Assert.fail("Something wrong on network connection.")
                return@runTest
            }
        }
        val sizeOfIds = result.size
        Assert.assertNotEquals(0, sizeOfIds)
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
                Assert.fail("Something wrong on network connection.")
                return@runTest
            }
        }
        Assert.assertNotEquals(GLOBAL_ID, minerStat.id)
    }

    companion object {
        private val SUPPORT_XMR_CONFIG = SupportXmrConfig()
        private const val ADDRESS =
            "86mmrEzBQ3RAiiZbEcnJXEZJsuTZE6oSXdwotPxNzEYxZQCfchfNFFYJzGMsii6DLYNxAxW5sGwDBEWvqq9tBsKyBWaKtX1"
        private const val IDENTIFIER = "YC-PC"
        private const val GLOBAL_ID = "global"
    }
}