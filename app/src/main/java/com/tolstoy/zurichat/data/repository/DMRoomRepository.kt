package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.localSource.dao.RoomDao
import com.tolstoy.zurichat.data.remoteSource.DMRoomService
import com.tolstoy.zurichat.models.Room
import com.tolstoy.zurichat.models.network_response.CreateRoom
import com.tolstoy.zurichat.models.network_response.RoomInfoResponse
import javax.inject.Inject

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 25-Sep-21
 */
class DMRoomRepository @Inject constructor(
    private val dmRoomService: DMRoomService,
    private val dao: RoomDao
) {
    private val roomsCache = mutableListOf<Room>()
    private val roomInfoCache = mutableListOf<RoomInfoResponse>()

    suspend fun getRooms(userId: String) = roomsCache.apply {
        if(isEmpty()) addAll(dmRoomService.getRooms(auth, userId))
    }

    suspend fun getRoomInfo(roomId: String):  RoomInfoResponse {
        return roomInfoCache.firstOrNull { roomId == it.id } ?:
        dmRoomService.getRoomInfo(auth, roomId).also {
            roomInfoCache.add(it)
        }
    }

    suspend fun createRoom(room: CreateRoom) = dmRoomService.createRoom(auth, room)
}