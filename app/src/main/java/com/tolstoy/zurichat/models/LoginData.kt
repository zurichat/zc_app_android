package com.tolstoy.zurichat.models

data class LoginData(
    val email: String,
    val token: String,
    val user_id: String
)
