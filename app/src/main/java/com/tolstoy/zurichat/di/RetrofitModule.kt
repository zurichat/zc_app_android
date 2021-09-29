package com.tolstoy.zurichat.di


import android.content.SharedPreferences
import com.google.gson.Gson
import com.tolstoy.zurichat.data.remoteSource.ChatsService
import com.tolstoy.zurichat.data.remoteSource.FilesService
import com.tolstoy.zurichat.data.remoteSource.Retrofit as RetrofitBuilder
import com.tolstoy.zurichat.data.remoteSource.UsersService
import com.tolstoy.zurichat.data.remoteSource.DMRoomService
import com.tolstoy.zurichat.util.USER_TOKEN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
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
    fun provideClient(interceptor: HttpLoggingInterceptor, sharedPreferences : SharedPreferences): OkHttpClient {
        // Add authorization token to the header interceptor
        val headerAuthorization = Interceptor { chain ->
            val request = chain.request().newBuilder()
            sharedPreferences.getString(USER_TOKEN, null)?.let {
                request.addHeader("Authorization", "Bearer $it")
            }
            chain.proceed(request.build())
        }
        return OkHttpClient.Builder().
        addInterceptor(headerAuthorization).
        addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.zuri.chat/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    fun provideUserService(retrofit: Retrofit): UsersService =
        retrofit.create(UsersService::class.java)

    @Provides
    fun provideChatService() =
        RetrofitBuilder.retrofit(ChatsService.BASE_URL).create(ChatsService::class.java)

    @Provides
    fun provideRoomService() =
        RetrofitBuilder.retrofit(DMRoomService.BASE_URL).create(DMRoomService::class.java)

    @Provides
    fun provideFileService() =
        RetrofitBuilder.retrofit(FilesService.BASE_URL).create(FilesService::class.java)
}