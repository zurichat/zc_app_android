package com.tolstoy.zurichat.ui.dm_chat.apiservice

import com.tolstoy.zurichat.ui.dm_chat.utils.MyInterceptor
import com.tolstoy.zurichat.ui.dm_chat.utils.RoomConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val client = OkHttpClient.Builder().apply {
        addInterceptor(MyInterceptor())
    }.build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(RoomConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val retrofitService: ApiDMService by lazy {
        retrofit.create(ApiDMService::class.java)
    }

    private val retrofit2 by lazy {
        Retrofit.Builder()
            .baseUrl(RoomConstants.BASE_URL1)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val retrofitService2: ApiDMService by lazy {
        retrofit2.create(ApiDMService::class.java)
    }
}