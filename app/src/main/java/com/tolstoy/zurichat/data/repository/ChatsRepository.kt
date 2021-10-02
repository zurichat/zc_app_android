package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.remoteSource.ChatsService
import com.tolstoy.zurichat.models.Message
import javax.inject.Inject

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 25-Sep-21 at 3:53 AM
 */
class ChatsRepository @Inject constructor(private val service: ChatsService) {

    suspend fun getMessages(roomId: String) = service.getMessages(roomId)

    suspend fun getMessage(roomId: String, messageId: String) =
        service.getMessage(auth, roomId, messageId)

    suspend fun sendMessage(roomId: String, message: Message) =
        service.sendMessage(auth, roomId, message)
}