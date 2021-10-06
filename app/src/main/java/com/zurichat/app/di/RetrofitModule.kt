package com.zurichat.app.di

import android.app.Application
import android.content.SharedPreferences
import com.google.gson.Gson
import com.zurichat.app.data.remoteSource.DMService
import com.zurichat.app.data.remoteSource.FilesService
import com.zurichat.app.data.remoteSource.UsersService
import com.zurichat.app.data.remoteSource.hasNetwork
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
import java.util.concurrent.TimeUnit


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun provideGson(): Gson {
        return Gson().newBuilder().setLenient().create()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().also {
            it.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideRetrofitCache(application: Application) =
        // creates a cache with a max size of 10mb
        Cache(application.applicationContext.cacheDir, RETROFIT_CACHE_SIZE)


    @Provides
    fun provideClient(
        cache: Cache, application: Application,
        interceptor: HttpLoggingInterceptor,
        sharedPreferences: SharedPreferences
    ): OkHttpClient {
        // Add authorization token to the header interceptor
        val headerAuthorization = Interceptor { chain ->
            val request = chain.request().newBuilder()
            sharedPreferences.getString(USER_TOKEN, null)?.let {
                request.addHeader("Authorization", "Bearer $it")
            }
            chain.proceed(request.build())
        }
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
            .addInterceptor(headerAuthorization)
            .addInterceptor(interceptor)
            .addInterceptor(cacheInterceptor)
            .connectionPool(ConnectionPool(0, 1, TimeUnit.MICROSECONDS))
            .protocols(listOf(Protocol.HTTP_1_1))
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideRetrofitBuilder(client: OkHttpClient, gson: Gson): Retrofit.Builder =
        Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))

    @Provides
    fun provideUserService(builder: Retrofit.Builder): UsersService =
        builder.baseUrl("https://api.zuri.chat/").build().create(UsersService::class.java)

    @Provides
    fun provideDMService(builder: Retrofit.Builder) =
        builder.baseUrl(DMService.BASE_URL).build().create(DMService::class.java)

    @Provides
    fun provideFileService(builder: Retrofit.Builder) =
        builder.baseUrl(FilesService.BASE_URL).build().create(FilesService::class.java)
}