package com.tolstoy.zurichat.ui.dm_chat.utils

import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.dm_chat.utils.RoomConstants.Companion.token
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