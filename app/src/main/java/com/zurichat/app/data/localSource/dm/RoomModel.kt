package com.zurichat.app.data.localSource.dm

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_messages")
data class RoomModel(
    @PrimaryKey(autoGenerate = false)
    var user_id: String,
    var user_name: String,
    var message: String,
    var timeStamp: String
)