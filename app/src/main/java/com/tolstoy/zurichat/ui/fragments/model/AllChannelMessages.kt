package com.tolstoy.zurichat.ui.fragments.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase.TypeConverters.DataTypeConverter

@Entity(tableName = "ChannelMessages")
data class AllChannelMessages(
    @TypeConverters(DataTypeConverter::class)
    var `data`: List<Data>,
    val message: String,
    val status: Int
){
    @PrimaryKey
    var channelId: String = ""
}

class Notifications