package com.tolstoy.zurichat.data.localSource.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val email: String,
    val token: String,
    val displayName: String,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val timeZone: String,
    val createdAt: String,
    val status: Int,
    val updatedAt: String
)