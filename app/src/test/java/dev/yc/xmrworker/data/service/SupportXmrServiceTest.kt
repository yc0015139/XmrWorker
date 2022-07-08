package dev.yc.xmrworker.data.service

import dev.yc.xmrworker.data.remote.config.SupportXmrConfig
import dev.yc.xmrworker.data.service.generator.ServiceGenerator
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class SupportXmrServiceTest {
    private val service: SupportXmrService = ServiceGenerator(
        config = SUPPORT_XMR_CONFIG,
    ).createService(SupportXmrService::class.java)

    @Test
    fun fetchWorkers_workersNotNull() = runTest {
        val workersResp = service.fetchWorkers(ADDRESS)
        val workers = workersResp.body()
        Assert.assertNotNull(workers)
    }

    @Test
    fun fetchIdentifiers_sizeOfIdentifiersIsNotZero() = runTest {
        val identifiersResp = service.fetchIdentifiers(ADDRESS)
        val sizeOfIdentifier = identifiersResp.body()?.size
        Assert.assertNotEquals(0, sizeOfIdentifier)
    }

    @Test
    fun fetchMinerStats_idOfMinerIsNotGlobalId() = runTest {
        val minerStatResp = service.fetchMinerStatByIdentifier(
            addressOfWallet = ADDRESS,
            id = IDENTIFIER,
        )
        val minerStat = minerStatResp.body()
        val idOfMiner = minerStat?.id
        Assert.assertNotEquals(GLOBAL_ID, idOfMiner)
    }

    companion object {
        private val SUPPORT_XMR_CONFIG = SupportXmrConfig()
        private const val ADDRESS =
            "86mmrEzBQ3RAiiZbEcnJXEZJsuTZE6oSXdwotPxNzEYxZQCfchfNFFYJzGMsii6DLYNxAxW5sGwDBEWvqq9tBsKyBWaKtX1"
        private const val IDENTIFIER = "YC-PC"
        private const val GLOBAL_ID = "global"
    }
}