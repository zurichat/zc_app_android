package com.tolstoy.zurichat.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.util.Patterns
import org.jsoup.Jsoup
import retrofit2.Call
import timber.log.Timber

/*
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created 02-Sep-21 at 7:56 PM
 */
fun String.extractUrl(): String? {
    val matcher = Patterns.WEB_URL.matcher(this)
    if(matcher.find()){
        return matcher.group()
    }
    return null
}

suspend fun String.getWebsiteMetadata() = try {
    val TAG = "Jsoup"
    val url = if (startsWith("http://") || startsWith("https://")) this else "http://$this"
    Timber.d("getWebsiteMetadata: before get $url")
    val result = Jsoup.connect(url).timeout(0).get()
    Timber.d("getWebsiteMetadata: after get $result")
    result
} catch (exception: Exception) { null }

fun Context.hasNetwork(): Boolean{
    var isConnected = false
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    if(activeNetwork != null && activeNetwork.isConnected)
        isConnected = true
    return isConnected
}
