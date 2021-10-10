package com.zurichat.app.ui.fragments.networking

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

 object RetrofitClientInstance {
    private var retrofit: Retrofit? = null
     private var retrofitUser: Retrofit? = null

    // Gets a reference to the client holding the logging interceptor to view network interactions
    private val interceptor = HttpLoggingInterceptor().also {
         it.level = HttpLoggingInterceptor.Level.BODY
     }
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    private const val BASE_URL = "https://channels.zuri.chat/api/"
     private const val BASE_URL_USER = "https://api.zuri.chat/"

    val retrofitInstance: Retrofit? get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

     val retrofitInstanceForUser: Retrofit? get() {
         if (retrofitUser == null) {
             retrofitUser = Retrofit.Builder()
                 .baseUrl(BASE_URL_USER)
                 .client(client)
                 .addConverterFactory(GsonConverterFactory.create())
                 .build()
         }
         return retrofitUser
     }
}