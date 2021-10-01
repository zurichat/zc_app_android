package com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase

import androidx.room.*

@Dao
interface RoomDao {
    @Query("SELECT * FROM roomdata")
    fun getAllRoomData(): List<RoomDataObject>

    @Query("SELECT * FROM roomdata WHERE channel_id LIKE :channelID LIMIT 1")
    fun getRoomDataWithChannelID(channelID: String) : RoomDataObject

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg roomDataObject: RoomDataObject)

    @Delete
    fun delete(vararg roomDataObject: RoomDataObject)
}