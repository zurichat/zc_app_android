package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.Message
import com.tolstoy.zurichat.models.network_response.*
import com.tolstoy.zurichat.ui.dm_chat.model.response.message.GetMessageResponse
import retrofit2.http.*

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 18-Sep-21
 */
interface ChatsService {

    @GET("rooms{room_id}/messages")
    suspend fun getMessages(@Path("room_id") roomId: String): GetMessagesResponse


    @GET("dmgetmessage/{room_id}/{message_id}")
    suspend fun getMessage(@Header("Authorization") authToken: String?,
                           @Path("room_id") roomId: String,
                           @Path("message_id") messageId: String): Message

    @POST("rooms/{room_id}/messages")
    suspend fun sendMessage(@Header("Authorization") authToken: String?,
                            @Path("room_id") roomId: String,
                            @Body message: Message): SendMessageResponse



    companion object {
        const val BASE_URL = "https://dm.zuri.chat/dmapi/v1/org/614679ee1a5607b13c00bcb7/"
        const val DM_API = "dmapi/v1/6145eee9285e4a18402074cd/"
    }
}