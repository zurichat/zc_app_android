package com.zurichat.app.data.remoteSource

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import com.zurichat.app.util.Result
import retrofit2.Response

/*
* Jeffrey Orazulike [https://github.com/jeffreyorazulike]
* Created on 10/2/2021 at 9:00 PM 
*/

/**
 * @return a successful (containing the body of the response)
 * or failed (containing the cause of the failure) result
 * */
fun <T> Response<T>.result(): Result<T?> {
    return try{
        if(isSuccessful) Result.Success(body())
        else Result.Error(Throwable(
            if(errorBody()?.string().isNullOrBlank()) message() else errorBody()?.string()
        ))
    }catch(throwable: Throwable) {
        Result.Error(throwable)
    }
}

fun <T> MutableLiveData<T>.postValue(result: Result<T?>, error: MutableLiveData<String?>) {
    if (result is Result.Error) error.value = result.error.message
    else if (result is Result.Success) result.data?.let{ value = it }
}

fun Context.hasNetwork(): Boolean {
    var isConnected = false
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    if (activeNetwork != null && activeNetwork.isConnected)
        isConnected = true
    return isConnected
}

fun <T> Result<T>.retrieve(error: MutableLiveData<String?>?): T? {
    return if (this is Result.Success) this.data
    else {
        if (this is Result.Error) error?.value = this.error.message
        null
    }
}