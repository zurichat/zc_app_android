package com.tolstoy.zurichat.data.localSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tolstoy.zurichat.models.OrganizationMember
import com.tolstoy.zurichat.models.Room
import com.tolstoy.zurichat.models.User

/**
* Jeffrey Orazulike [https://github.com/jeffreyorazulike]
* Created on 9/29/2021 at 5:33 PM 
*/
@Dao
interface RoomDao{
    @Query("SELECT * FROM rooms WHERE isDmRoom LIKE:isDmRoom")
    suspend fun getRooms(isDmRoom: Boolean = true): List<User>

    @Query("SELECT * FROM rooms WHERE id LIKE:roomId")
    suspend fun getRoom(roomId: String): Room

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRoom(vararg room: Room)
}
