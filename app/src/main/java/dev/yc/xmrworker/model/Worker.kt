package dev.yc.xmrworker.model

import com.google.gson.annotations.SerializedName

data class Worker(
    @SerializedName("lts") val latestTimestampInSecond: Long,
    @SerializedName("identifer") val id: String,
    val hash: Long,
    val totalHash: Long,
)