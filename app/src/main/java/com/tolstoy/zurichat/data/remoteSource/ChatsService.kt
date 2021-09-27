package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.Message
import com.tolstoy.zurichat.models.network_response.*
import retrofit2.http.*

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 18-Sep-21
 */
interface ChatsService {

    @GET("${DM_API}{room_id}/messages")
    suspend fun getMessages(@Header("Authorization") authToken: String?,
                            @Path("room_id") roomId: String): GetMessagesResponse


    @GET("dmgetmessage/{room_id}/{message_id}")
    suspend fun getMessage(@Header("Authorization") authToken: String?,
                           @Path("room_id") roomId: String,
                           @Path("message_id") messageId: String): Message

    @POST("dmapi/v1/org/6145eee9285e4a18402074cd/rooms/{room_id}/messages")
    suspend fun sendMessage(@Header("Authorization") authToken: String?,
                            @Path("room_id") roomId: String,
                            @Body message: Message): SendMessageResponse



    companion object {
        const val BASE_URL = "https://dm.zuri.chat/"
        const val DM_API = "dmapi/v1/6145eee9285e4a18402074cd/"
    }
}