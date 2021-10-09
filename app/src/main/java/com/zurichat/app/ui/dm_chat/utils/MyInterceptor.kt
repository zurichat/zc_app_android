package com.zurichat.app.ui.dm_chat.utils

import com.zurichat.app.models.User
import okhttp3.Interceptor
import okhttp3.Response

class MyInterceptor: Interceptor {
    private var user: User? = null
    private var tokenB: String? = null
    override fun intercept(chain: Interceptor.Chain): Response {

        tokenB = user?.token
        val request = chain.request()
            .newBuilder()
            .addHeader("Authorization", "Bearer $tokenB")
            .build()
        return chain.proceed(request)
    }

}