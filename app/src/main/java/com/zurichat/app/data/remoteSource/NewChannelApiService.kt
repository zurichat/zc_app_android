package com.zurichat.app.data.remoteSource

import com.zurichat.app.models.CreateChannelBodyModel
import com.zurichat.app.models.CreateChannelResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface NewChannelApiService {
    @POST("1/channels/")
    suspend fun createChannel(@Body createChannelBodyModel: CreateChannelBodyModel): Response<CreateChannelResponseModel>

}
