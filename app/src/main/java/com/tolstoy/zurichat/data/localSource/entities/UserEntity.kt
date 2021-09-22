package com.tolstoy.zurichat.data.localSource.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    val created_at: String,
    val display_name: String,
    val email: String,
    val first_name: String,
    @PrimaryKey
    val id: String,
    val last_name: String,
    val phone: String,
    val status: Int,
    val time_zone: String,
    val updated_at: String,
    val token: String
)