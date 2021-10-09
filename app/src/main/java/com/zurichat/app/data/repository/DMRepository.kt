package com.zurichat.app.data.repository

import com.zurichat.app.data.remoteSource.DMService
import com.zurichat.app.data.remoteSource.enqueue
import com.zurichat.app.models.Message
import com.zurichat.app.models.network_response.CreateRoom
import javax.inject.Inject

/**
 * Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 10/2/2021 at 9:53 PM
 */
class DMRepository @Inject constructor(private val service: DMService) {

    suspend fun getMessages(orgId: String, roomId: String) =
        service.getMessages(orgId, roomId).enqueue()

    suspend fun getMessage(roomId: String, messageId: String) =
        service.getMessage(roomId, messageId).enqueue()

    suspend fun sendMessage(orgId: String, roomId: String, message: Message) =
        service.sendMessage(orgId, roomId, message).enqueue()

    suspend fun getRooms(orgId: String, userId: String) =
        service.getRooms(orgId, userId).enqueue()

    suspend fun getRoomInfo(orgId: String, roomId: String) =
        service.getRoomInfo(orgId, roomId).enqueue()

    suspend fun createRoom(orgId: String, userId: String, room: CreateRoom) =
        service.createRoom(orgId, userId, room).enqueue()

}
