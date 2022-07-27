package dev.yc.xmrworker.data.service

import dev.yc.xmrworker.data.ADDRESS
import dev.yc.xmrworker.data.GLOBAL_ID
import dev.yc.xmrworker.data.IDENTIFIER
import dev.yc.xmrworker.data.SUPPORT_XMR_CONFIG
import dev.yc.xmrworker.data.service.generator.ServiceGenerator
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle

@TestInstance(Lifecycle.PER_CLASS)
class SupportXmrServiceTest {
    private lateinit var service: SupportXmrService

    @BeforeAll
    fun setupService() {
        service = ServiceGenerator(
            config = SUPPORT_XMR_CONFIG
        ).createService(
            serviceClass = SupportXmrService::class.java
        )
    }

    @Test
    fun fetchWorkers_workersNotNull() = runTest {
        val workersResp = service.fetchWorkers(ADDRESS)
        val workers = workersResp.body()
        assertNotNull(workers)
    }

    @Test
    fun fetchIdentifiers_sizeOfIdentifiersIsNotZero() = runTest {
        val identifiersResp = service.fetchIdentifiers(ADDRESS)
        val sizeOfIdentifier = identifiersResp.body()?.size
        assertNotEquals(0, sizeOfIdentifier)
    }

    @Test
    fun fetchEachMinerData_idOfMinerIsNotGlobalId() = runTest {
        val minerDataResp = service.fetchEachMinerDataByIdentifier(
            addressOfWallet = ADDRESS,
            id = IDENTIFIER,
        )
        val minerData = minerDataResp.body()
        val idOfMiner = minerData?.id
        assertNotEquals(GLOBAL_ID, idOfMiner)
    }
}