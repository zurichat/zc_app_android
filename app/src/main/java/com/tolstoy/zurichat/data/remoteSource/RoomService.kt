package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.Room
import com.tolstoy.zurichat.models.network_response.CreateRoom
import com.tolstoy.zurichat.models.network_response.CreateRoomResponse
import com.tolstoy.zurichat.models.network_response.RoomInfoResponse
import retrofit2.Call
import retrofit2.http.*

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 25-Sep-21
 */

//https://dm.zuri.chat/api/v1/org/614679ee1a5607b13c00bcb7/users/61467ee61a5607b13c00bcf2/rooms


interface RoomService {
    @GET("users/{user_id}/rooms")
    fun getRooms(
        @Path("user_id") userId: String): Call<ArrayList<Room>>

    @GET("room-info")
    suspend fun getRoomInfo(@Header("Authorization") authToken: String?,
                            @Query("room_id") roomId: String): RoomInfoResponse

    @POST("room")
    suspend fun createRoom(
                           @Body roomInfo: CreateRoom
    ): CreateRoomResponse

    companion object {
        const val BASE_URL = "https://dm.zuri.chat/api/v1/org/614679ee1a5607b13c00bcb7/"
    }
}

//6145eee9285e4a18402074cd old orgId

//@Header("Authorization") authToken: String?,