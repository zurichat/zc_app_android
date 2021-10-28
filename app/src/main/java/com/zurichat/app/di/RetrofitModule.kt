package com.zurichat.app.di

import android.app.Application
import android.content.SharedPreferences
import com.google.gson.Gson
import com.zurichat.app.data.remoteSource.FilesService
import com.zurichat.app.data.remoteSource.UsersService
import com.zurichat.app.data.remoteSource.hasNetwork
import com.zurichat.app.ui.dm_chat.apiservice.ApiDMService
import com.zurichat.app.util.RETROFIT_CACHE_SIZE
import com.zurichat.app.util.USER_TOKEN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideGson(): Gson {
        return Gson().newBuilder().setLenient().create()
    }

    @Provides fun loggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides fun retrofitCache(application: Application) =
        // creates a cache with a max size of 10mb
        Cache(application.applicationContext.cacheDir, RETROFIT_CACHE_SIZE)

    @Provides fun headerAuthorization(sharedPreferences: SharedPreferences) = Interceptor { chain ->
        val request = chain.request().newBuilder()
        sharedPreferences.getString(USER_TOKEN, null)?.let {
            request.addHeader("Authorization", "Bearer $it")
        }
        chain.proceed(request.build())
    }

    @Provides
    fun okHttpClientBuilder(
        cache: Cache, application: Application,
        interceptor: HttpLoggingInterceptor,
        sharedPreferences: SharedPreferences
    ): OkHttpClient.Builder{
        // Add the cache interceptor that helps cache http responses on the users machine
        // in case of no network service
        val cacheInterceptor = Interceptor { chain ->
            var request = chain.request()
            request = if (application.applicationContext.hasNetwork())
                request.newBuilder().header(
                    "Cache-Control",
                    "public, max-age=" + 10
                ).build()
            else
                request.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7
                ).build()
            chain.proceed(request)
        }
        return OkHttpClient.Builder().cache(cache)
            .addInterceptor(interceptor)
            .addInterceptor(cacheInterceptor)
            .connectionPool(ConnectionPool(0, 1, TimeUnit.MICROSECONDS))
            .protocols(listOf(Protocol.HTTP_1_1))
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
    }

    @Provides
    fun retrofitBuilder(
        client: OkHttpClient.Builder,
        header: Interceptor,
        gson: Gson): Retrofit.Builder = Retrofit.Builder()
            .client(client.addInterceptor(header).build())
            .addConverterFactory(GsonConverterFactory.create(gson))

    @Provides
    fun provideUserService(builder: Retrofit.Builder): UsersService =
        builder.baseUrl("https://api.zuri.chat/").build().create(UsersService::class.java)

    @Provides
    fun roomService(client: OkHttpClient.Builder, gson: Gson) =
        Retrofit.Builder().client(client.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(ApiDMService.BASE_URL)
            .build()
    @Provides
    fun provideRoomApiService(retrofit: Retrofit): ApiDMService =
        retrofit.create(ApiDMService::class.java)

    @Provides
    fun provideFileService(builder: Retrofit.Builder) =
        builder.baseUrl(FilesService.BASE_URL).build().create(FilesService::class.java)
}