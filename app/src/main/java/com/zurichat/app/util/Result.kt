package com.zurichat.app.util

sealed class Result<out T> {
    object Loading: Result<Nothing>()
    data class Success<T>(val data: T): Result<T>()
    data class Error(val error: Throwable): Result<Nothing>()
}
