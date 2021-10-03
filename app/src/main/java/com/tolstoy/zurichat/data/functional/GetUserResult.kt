package com.tolstoy.zurichat.data.functional

sealed class GetUserResult<out T> {
    data class Success<R>(val data: R) : GetUserResult<R>()
    data class Error(val exp: Failure) : GetUserResult<Nothing>()
}
