package com.zurichat.app.data.remoteSource

import com.zurichat.app.models.CreateChannelBodyModel
import com.zurichat.app.models.CreateChannelResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface NewChannelApiService {
    @POST("{organization_id}/channels/")
    suspend fun createChannel(@Body createChannelBodyModel: CreateChannelBodyModel,@Path("organization_id") org_id: String): Response<CreateChannelResponseModel>
}
