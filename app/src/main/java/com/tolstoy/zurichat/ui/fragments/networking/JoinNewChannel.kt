package com.tolstoy.zurichat.ui.fragments.networking

import com.tolstoy.zurichat.ui.fragments.model.AllChannelMessages
import com.tolstoy.zurichat.ui.fragments.model.JoinChannelUser
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JoinNewChannel {
    // Post endpoint to join a new channel

    @POST("v1/{org_id}/channels/{channel_id}/members/")
    suspend fun joinChannel(@Path("org_id") organizationId : String, @Path("channel_id") channelId : String,@Body user : JoinChannelUser): JoinChannelUser

    // Endpoint to retrieve all messages in a channel
    @GET("v1/{org_id}/channels/{channel_id}/messages/")
    suspend fun retrieveAllMessages(@Path("org_id") organizationId : String, @Path("channel_id") channelId : String): AllChannelMessages

}