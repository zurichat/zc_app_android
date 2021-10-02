package com.tolstoy.zurichat.data.functional

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>() // Status success and data of the result
    data class Error(val failure: Failure) :
        Result<Nothing>() // Status Error an error message
    data class Failed<F>(val errorMessage: F):
        Result<F>()
    // string method to display a result for debugging
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$failure]"
            is Failed<*> -> "Failed[data=$errorMessage]"
        }
    }
}
