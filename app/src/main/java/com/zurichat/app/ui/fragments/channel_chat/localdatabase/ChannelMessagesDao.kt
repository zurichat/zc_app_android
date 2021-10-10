package com.zurichat.app.ui.fragments.channel_chat.localdatabase

import androidx.room.*
import com.zurichat.app.ui.fragments.model.AllChannelMessages

@Dao
interface ChannelMessagesDao {
    @Query("SELECT * FROM channelmessages")
    fun getAllChannelMessages(): List<AllChannelMessages>

    @Query("SELECT * FROM channelmessages WHERE channelId LIKE :channelID LIMIT 1")
    fun getChannelMessagesWithChannelID(channelID: String) : AllChannelMessages

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg data: AllChannelMessages)

    @Delete
    fun delete(vararg data: AllChannelMessages)
}