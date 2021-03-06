package com.zurichat.app.ui.fragments.home_screen.chats_and_channels.localdatabase

import androidx.room.*

@Dao
interface ChannelListDao {
    @Query("SELECT * FROM channellist")
    fun getAllChannels(): List<ChannelListObject>

    @Query("SELECT * FROM channellist WHERE organization_id LIKE :orgID LIMIT 1")
    fun getChannelListWithOrganizationID(orgID: String) : ChannelListObject

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg data: ChannelListObject)

    @Delete
    fun delete(vararg data: ChannelListObject)
}