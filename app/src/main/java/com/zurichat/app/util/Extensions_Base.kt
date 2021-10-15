package com.zurichat.app.util

import android.util.Log
import android.util.Patterns
import org.jsoup.Jsoup

/**
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
    val url = if(startsWith("http://") || startsWith("https://")) this else "http://$this"
    Log.d(TAG, "getWebsiteMetadata: before get $url")
    val result = Jsoup.connect(url).timeout(0).get()
    Log.d(TAG, "getWebsiteMetadata: after get $result")
    result
} catch (exception: Exception) { null }
