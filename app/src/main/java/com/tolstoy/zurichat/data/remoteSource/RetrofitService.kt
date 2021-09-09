package com.tolstoy.zurichat.data.remoteSource

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create


//   https://api.zuri.chat/auth/login
//    https://api.zuri.chat/users

private  val BASE_URL = "https://api.zuri.chat/auth/login "
interface RetrofitService {


}

        //add retrofit library
val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL).build()

object Api {
    val retrofitService: RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }
}
