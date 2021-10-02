package com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels.localdatabase

import androidx.room.*
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.ui.fragments.model.AllChannelMessages
import com.tolstoy.zurichat.ui.fragments.model.Data

@Dao
interface ChannelListDao {
    @Query("SELECT * FROM channellist")
    fun getAllChannels(): List<ChannelModel>

    @Query("SELECT * FROM channellist WHERE _id LIKE :channelID LIMIT 1")
    fun getChannelListWithChannelID(channelID: String) : ChannelModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg data: ChannelModel)

    @Delete
    fun delete(vararg data: ChannelModel)
}