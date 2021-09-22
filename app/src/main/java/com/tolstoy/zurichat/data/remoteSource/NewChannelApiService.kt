package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.CreateChannelBodyModel
import com.tolstoy.zurichat.models.CreateChannelResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NewChannelApiService {
    @POST("1/channels/")
    suspend fun createChannel(@Body createChannelBodyModel: CreateChannelBodyModel): Response<CreateChannelResponseModel>

}
