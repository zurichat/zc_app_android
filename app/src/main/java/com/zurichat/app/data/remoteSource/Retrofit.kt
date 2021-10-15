package com.zurichat.app.data.remoteSource

import com.zurichat.app.data.repository.auth
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 18-Sep-21
 */
object Retrofit {
    // Gets a reference to the client holding the logging interceptor to view network interactions
    private val interceptor = HttpLoggingInterceptor().also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }
    private var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $auth")
            .build()
        chain.proceed(newRequest)
    }).build()

    fun retrofit(baseUrl: String): Retrofit =
        Retrofit.Builder().baseUrl(baseUrl).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}