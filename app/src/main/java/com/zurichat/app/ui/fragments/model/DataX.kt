package com.zurichat.app.ui.fragments.model

data class DataX(
    val Organizations: List<String>,
    val _id: String,
    val created_at: String,
    val deactivated: Boolean,
    val deactivated_at: String,
    val email: String,
    val email_verification: Any,
    val first_name: String,
    val isverified: Boolean,
    val last_name: String,
    val password_resets: Any,
    val phone: String,
    val settings: Any,
    val time_zone: String,
    val updated_at: String,
    val workspaces: Any
)