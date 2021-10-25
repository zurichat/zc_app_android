package com.zurichat.app.data.localSource.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zurichat.app.models.RoomModel

@Dao
interface RoomMessageDao {
    
    @Query("SELECT * FROM room_messages WHERE user_id LIKE :userID")
    fun getRoomMessageWithUserID(userID: String) : LiveData<List<RoomModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(roomMessages: List<RoomModel>)

}