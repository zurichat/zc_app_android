package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.remoteSource.RoomService
import com.tolstoy.zurichat.models.Room
import com.tolstoy.zurichat.models.network_response.CreateRoom
import com.tolstoy.zurichat.models.network_response.RoomInfoResponse
import javax.inject.Inject

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 25-Sep-21
 */
class RoomRepository @Inject constructor(private val service: RoomService) {
    private val roomsCache = mutableListOf<Room>()
    private val roomInfoCache = mutableListOf<RoomInfoResponse>()

    suspend fun getRooms(userId: String) = roomsCache.apply {
       // if(isEmpty()) addAll(service.getRooms(userId))
    }

    suspend fun getRoomInfo(roomId: String):  RoomInfoResponse {
        return roomInfoCache.firstOrNull { roomId == it.id } ?:
        service.getRoomInfo(auth, roomId).also {
            roomInfoCache.add(it)
        }
    }

    suspend fun createRoom(room: CreateRoom) = service.createRoom(room)
}