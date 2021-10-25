package com.zurichat.app.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.ZoneOffset

@Parcelize
@Entity(tableName = "starred_messages")
data class StarredMessages(
    @PrimaryKey
    val id: String,
    val createdAt: String = LocalDateTime.now(ZoneOffset.UTC)
        .toString(), // 2021-09-18T16:39:54.008Z
    val senderId: String,
    val roomId: String,
    val message: String
) : Parcelable
