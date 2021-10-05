package com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels.localdatabase

import androidx.room.*

@Dao
interface AllChannelListDao {
    @Query("SELECT * FROM allchannellist")
    fun getAllChannels(): List<AllChannelListObject>

    @Query("SELECT * FROM allchannellist WHERE organization_id LIKE :orgID LIMIT 1")
    fun getChannelListWithOrganizationID(orgID: String) : AllChannelListObject

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg data: AllChannelListObject)

    @Delete
    fun delete(vararg data: AllChannelListObject)
}