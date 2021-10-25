package com.zurichat.app.data.localSource.dao

import androidx.room.*
import com.zurichat.app.models.StarredMessages
import kotlinx.coroutines.flow.Flow

@Dao
interface StarredMessagesDao {

    @Query("SELECT * FROM starred_messages")
    fun getStarredMessages(): Flow<List<StarredMessages>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun starMessage(starredMessages: StarredMessages)

    @Delete
    suspend fun unStarMessage(starredMessages: StarredMessages)
}