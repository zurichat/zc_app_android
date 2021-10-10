package com.zurichat.app.models

data class ResetCodeBody (
    val email:String,
    val code: String
        )