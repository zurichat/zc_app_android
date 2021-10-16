package com.zurichat.app.data.localSource.dm

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoomMessageDao {
    
    @Query("SELECT * FROM room_messages WHERE user_id LIKE :userID")
    fun getRoomMessageWithUserID(userID: String) : LiveData<List<RoomModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(roomMessages: List<RoomModel>)

}