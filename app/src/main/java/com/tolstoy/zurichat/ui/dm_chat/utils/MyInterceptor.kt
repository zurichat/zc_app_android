package com.tolstoy.zurichat.ui.dm_chat.utils

import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer ${RoomConstants.token}")
            .build()
        return chain.proceed(request)
    }

}