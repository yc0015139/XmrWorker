package dev.yc.xmrworker.data.remote

sealed interface ApiResult<out T> {
    data class Success<out T>(val result: T) : ApiResult<T>

    data class Error(val code: Int) : ApiResult<Nothing>

    object Exception : ApiResult<Nothing>
}