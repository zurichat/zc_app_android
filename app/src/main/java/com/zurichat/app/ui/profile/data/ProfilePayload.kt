package com.zurichat.app.ui.profile.data

data class ProfilePayload(
    val bio: String,
    val display_name: String,
    val phone: String
)

data class PhoneUpdate(
    val phone: String
)

data class AboutUpdate(
    val bio: String
)

data class NameUpdate(
    val display_name: String
)