package com.zurichat.app.ui.fragments.channel_chat.localdatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RoomData")
class RoomDataObject {
    @PrimaryKey
    @ColumnInfo(name = "channel_id")
    var channelId: String = ""

    @ColumnInfo(name = "socket_name")
    var socketName: String = ""
}