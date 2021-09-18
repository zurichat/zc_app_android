package com.tolstoy.zurichat.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val created_at: String,
    val display_name: String,
    val email: String,
    val first_name: String,
    val id: String,
    val last_name: String,
    val phone: String,
    val status: Int,
    val time_zone: String,
    val token: String,
    val updated_at: String
): Parcelable
