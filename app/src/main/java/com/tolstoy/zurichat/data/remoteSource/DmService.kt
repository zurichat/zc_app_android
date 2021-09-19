package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.Message
import com.tolstoy.zurichat.models.Room
import com.tolstoy.zurichat.models.network_response.MessageLinkResponse
import com.tolstoy.zurichat.models.network_response.MessageResponse
import com.tolstoy.zurichat.models.network_response.RoomInfoResponse
import retrofit2.http.*

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 18-Sep-21
 */
interface DmService {

    @GET("api/v1/messages/{room_id}")
    suspend fun getMessages(@Header("Authorization") authToken: String?,
                            @Path("room_id") roomId: String): MessageResponse

    @GET("api/v1/get_organization_members")
    suspend fun getOrganizationMembers(@Header("Authorization") authToken: String?)

    @GET("api/v1/getuserrooms")
    suspend fun getRooms(@Header("Authorization") authToken: String?,
                         @Query("user_id") userId: String): List<RoomInfoResponse>

    @GET("api/v1/room-info")
    suspend fun getRoomInfo(@Header("Authorization") authToken: String?,
                         @Query("room_id") roomId: String): RoomInfoResponse

    @GET("getmessage/{room_id}/{message_id}")
    suspend fun getMessage(@Header("Authorization") authToken: String?,
                           @Path("room_id") roomId: String,
                           @Path("message_id") messageId: String): MessageLinkResponse

    @POST("api/v1/rooms/{room_id}/message")
    suspend fun sendMessage(@Header("Authorization") authToken: String?,
                            @Path("room_id") roomId: String,
                            @Body message: Message): MessageResponse

    companion object {
        val BASE_URL = "https://dm.zuri.chat/"

        val dmServiceImpl by lazy {
            Retrofit.retrofit(BASE_URL).create(DmService::class.java)
        }
    }
}