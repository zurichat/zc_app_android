package com.zurichat.app.ui.profile.data

data class ProfileRequest(
    val bio: String = "",
    val display_name: String = "",
    val first_name: String = "",
    val last_name: String = "",
    val phone: String = "",
    val pronouns: String = "",
    val socials: List<Social>? = null,
    val time_zone: String = ""
)

data class Social(
    val title: String,
    val url: String
)