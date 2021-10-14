package com.zurichat.app.ui.profile.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class DataX(
    val _id: String,
    val bio: String,
    val deleted: Boolean,
    val deleted_at: String,
    val display_name: String,
    val email: String,
    //val files: Any,
    val first_name: String,
    val id: String,
    val image_url: String,
    val joined_at: String,
    val last_name: String,
    val name: String,
    val org_id: String,
    val phone: String,
    val presence: String,
    val pronouns: String,
    val role: String,
    //val settings: Any,
    //val socials: Any,
    val status: Status,
    val time_zone: String,
    val user_name: String
): Parcelable

@Parcelize
data class Status(
    val expiry_time : String,
   // val status_history : Any,
    val tag : String,
    val text : String
):Parcelable
