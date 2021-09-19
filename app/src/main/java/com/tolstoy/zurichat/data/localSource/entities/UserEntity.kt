package com.tolstoy.zurichat.data.localSource.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
 data class UserEntity (
    @PrimaryKey
    val email: String,
    val token: String
)