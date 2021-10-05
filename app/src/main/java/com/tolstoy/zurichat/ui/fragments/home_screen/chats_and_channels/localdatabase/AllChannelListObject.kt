package com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels.localdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels.localdatabase.TypeConverter.ChannelListConverter

@Entity(tableName = "AllChannelList")
data class AllChannelListObject(@TypeConverters(ChannelListConverter::class)val channelList: List<ChannelModel>) {
    @PrimaryKey
    @ColumnInfo(name = "organization_id")
    var orgId: String = ""
}