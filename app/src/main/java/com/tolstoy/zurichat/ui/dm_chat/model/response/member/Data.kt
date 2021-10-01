package com.tolstoy.zurichat.ui.dm_chat.model.response.member

data class Data(
    val _id: String,
    val bio: String,
    val deleted: Boolean,
    val deleted_at: String,
    val display_name: String,
    val email: String,
    val files: Any,
    val first_name: String,
    val id: String,
    val image_url: String,
    val joined_at: String,
    val language: String,
    val last_name: String,
    val org_id: String,
    val phone: String,
    val presence: String,
    val pronouns: String,
    val role: String,
    val settings: Settings,
    val socials: Any,
    val status: String,
    val time_zone: String,
    val user_name: String
)