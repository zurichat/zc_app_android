package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.Room
import com.tolstoy.zurichat.models.network_response.CreateRoom
import com.tolstoy.zurichat.models.network_response.CreateRoomResponse
import com.tolstoy.zurichat.models.network_response.RoomInfoResponse
import retrofit2.http.*

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 25-Sep-21
 */
interface RoomService {
    @GET("rooms/{user_id}")
    suspend fun getRooms(@Header("Authorization") authToken: String?,
                         @Path("user_id") userId: String): List<Room>

    @GET("room-info")
    suspend fun getRoomInfo(@Header("Authorization") authToken: String?,
                            @Query("room_id") roomId: String): RoomInfoResponse

    @POST("createroom")
    suspend fun createRoom(@Header("Authorization") authToken: String?,
                           @Body roomInfo: CreateRoom
    ): CreateRoomResponse

    companion object {
        const val BASE_URL = "https://dm.zuri.chat/dmapi/v1/6145eee9285e4a18402074cd/"
    }
}