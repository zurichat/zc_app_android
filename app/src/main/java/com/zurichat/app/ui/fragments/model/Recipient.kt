package com.zurichat.app.ui.fragments.model

data class Recipient(
    val _id: String,
    val is_admin: Boolean,
    val notifications: Notifications,
    val role_id: String
)

