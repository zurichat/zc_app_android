package com.tolstoy.zurichat.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "users")
data class User(
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
    val token: String,
    val currentUser: Boolean = false
): Parcelable