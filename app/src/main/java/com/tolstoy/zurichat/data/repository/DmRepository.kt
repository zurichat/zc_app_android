package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.localSource.Cache
import com.tolstoy.zurichat.data.remoteSource.DmService.Companion.dmServiceImpl
import com.tolstoy.zurichat.models.Message

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 19-Sep-21
 */
class DmRepository {
    private val auth = Cache.map.getOrDefault("USER", "").toString()

    suspend fun getMessages(roomId: String) = dmServiceImpl.getMessages(auth,roomId)

    suspend fun getOrganizationMembers() = dmServiceImpl.getOrganizationMembers(auth)

    suspend fun getRooms(userId: String) = dmServiceImpl.getRooms(auth,userId)

    suspend fun getRoomInfo(roomId: String) = dmServiceImpl.getRoomInfo(auth, roomId)

    suspend fun getMessage(roomId: String, messageId: String) =
        dmServiceImpl.getMessage(auth, roomId, messageId)

    suspend fun sendMessage(roomId: String, message: Message) =
        dmServiceImpl.sendMessage(auth, roomId, message)

    companion object {
        val INSTANCE by lazy { DmRepository() }
    }
}