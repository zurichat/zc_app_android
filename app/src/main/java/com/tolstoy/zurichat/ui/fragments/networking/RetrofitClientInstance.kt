package com.tolstoy.zurichat.ui.fragments.networking

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal object RetrofitClientInstance {
    private var retrofit: Retrofit? = null
    private const val BASE_URL = "https://channels.zuri.chat/api/"
    val retrofitInstance: Retrofit? get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
            }
            return retrofit
        }
}