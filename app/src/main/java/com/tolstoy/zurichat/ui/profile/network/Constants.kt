package com.tolstoy.zurichat.ui.profile.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object Constants {

    const val BASE_URL: String = "https://api.zuri.chat/"
    const val BASE_URL1: String = "https://dm.zuri.chat/api/v1/org/614679ee1a5607b13c00bcb7/"

    //Ensure the phone has internet connectivity. Make sure internet is turn on.
    fun isNetworkAvailable(context: Context) : Boolean {
        val connectivityManager = context.
        getSystemService(Context.CONNECTIVITY_SERVICE) as
                ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return  networkInfo != null && networkInfo.isConnectedOrConnecting
        }
    }
}