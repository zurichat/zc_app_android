package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.util.Result
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/*
* Jeffrey Orazulike [https://github.com/jeffreyorazulike]
* Created on 9/29/2021 at 4:22 PM 
*/

/**
 * An extension function that tries to make the enqueue method of a retrofit call suspendable
 * by eliminating callbacks
 *
 * @return a successful (containing the response) or failed (containing the cause of the failure) result
 * */
@ExperimentalCoroutinesApi
suspend fun <T> Call<T>.enqueue(): Result<Response<T>> {
    return suspendCancellableCoroutine { cont: CancellableContinuation<Result<Response<T>>> ->
        enqueue(
            object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    cont.resume(Result.Success(response)) { Timber.e("onResponse: $it") }
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    cont.resume(Result.Failure(t)) { Timber.e("onFailure: $it and $t") }
                }
            }
        )
    }
}