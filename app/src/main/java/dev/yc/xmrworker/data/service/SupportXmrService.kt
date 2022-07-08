package dev.yc.xmrworker.data.service

import dev.yc.xmrworker.model.MinerStat
import dev.yc.xmrworker.model.Worker
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SupportXmrService {
    @GET("api/miner/{address}/stats/allWorkers")
    suspend fun fetchWorkers(
        @Path("address") addressOfWallet: String? = null,
    ): Response<Map<String, Worker>>
    /*
        {
            "global": {
                "lts": 1111111111,
                "identifer": "global",
                "hash": 9999,
                "totalHash": 999999999
            },
            "Name-of-worker-a": {
                "lts": 1111111111,
                "identifer": "Name-of-worker-a",
                "hash": 9999,
                "totalHash": 999999999
            },
            "Name-of-worker-b": {
                "lts": 1111111111,
                "identifer": "Name-of-worker-b",
                "hash": 9999,
                "totalHash": 999999999
            }
        }
    */

    @GET("api/miner/{address}/identifiers")
    suspend fun fetchIdentifiers(
        @Path("address") addressOfWallet: String? = null,
    ): Response<List<String>>
    /*
        {
            ["Name-of-worker-a", "Name-of-worker-b"]
        }
    */

    @GET("api/miner/{address}/stats/{identifier}")
    suspend fun fetchMinerStatByIdentifier(
        @Path("address") addressOfWallet: String? = null,
        @Path("identifier") id: String? = null,
    ): Response<MinerStat>
    /*
        {
            "lts": 1657266133,
            "identifer": "Name-of-worker-a",
            "hash": "6081",
            "totalHash": "81130718490",
            "validShares": 269930,
            "invalidShares": 5
        }
    */
}