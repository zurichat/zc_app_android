package com.tolstoy.zurichat.data.remoteSource

import androidx.lifecycle.MutableLiveData
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
 * In that case you only receive a single result back from the request and don't bother handling
 * callbacks
 *
 * @return a successful (containing the response) or failed (containing the cause of the failure) result
 * */
@ExperimentalCoroutinesApi
suspend fun <T> Call<T>.enqueue(): Result<T?> {
    return suspendCancellableCoroutine { cont: CancellableContinuation<Result<T?>> ->
        enqueue(
            object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) = with(response) {
                    cont.resume(
                        try{
                            if(isSuccessful) Result.Success(body())
                            else Result.Failure(errorBody().toString())
                        }catch(throwable: Throwable) {
                            Result.Failure(throwable)
                        }
                    ){ Timber.tag("Suspendable Retrofit Call").e("onResponse: $it") }
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    cont.resume(Result.Failure(t)) {
                        Timber.tag("Suspendable Retrofit Call").e("onFailure: $it and $t")
                    }
                }
            }
        )
    }
}

fun <T> MutableLiveData<T>.postValue(result: Result<T?>, error: MutableLiveData<String?>){
    if(result is Result.Failure) error.value = result.error.message
    else if(result is Result.Success) value = result.data!!
}

fun <T> Result<T>.retrieve(error: MutableLiveData<String?>?): T?{
    return if(this is Result.Success) this.data
    else {
        if(this is Result.Failure) error?.value = this.error.message
        null
    }
}