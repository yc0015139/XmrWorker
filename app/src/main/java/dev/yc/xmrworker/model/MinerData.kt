package dev.yc.xmrworker.model

import com.google.gson.annotations.SerializedName

data class MinerData(
    val hash: String,
    @SerializedName("identifer") val id: String,
    val invalidShares: Int,
    val lts: Int,
    val totalHash: String,
    val validShares: Int
)
