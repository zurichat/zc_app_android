package com.zurichat.app.models

data class UpdatePassBody (
    val email : String,
    val code : String,
    val password : String,
    val confirm_password: String
        )
