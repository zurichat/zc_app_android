package com.zurichat.app.data.repository

import com.zurichat.app.data.localSource.dao.StarredMessagesDao
import com.zurichat.app.models.StarredMessages
import kotlinx.coroutines.flow.Flow

class StarredMessagesRepository(private val starredMessagesDao: StarredMessagesDao) {

    val getAllStarredMessages: Flow<List<StarredMessages>> = starredMessagesDao.getStarredMessages()

    suspend fun starMessage(starredMessages: StarredMessages) {
        starredMessagesDao.starMessage(starredMessages)
    }

    suspend fun unStarMessage(starredMessages: StarredMessages) {
        starredMessagesDao.unStarMessage(starredMessages)
    }
}