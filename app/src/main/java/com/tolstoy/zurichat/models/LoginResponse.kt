package com.tolstoy.zurichat.models

data class LoginResponse(
    val `data`: DataX,
    val message: String,
    val status: Int
)