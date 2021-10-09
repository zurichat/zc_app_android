package com.zurichat.app.models

data class ResetCodeResponse (
    val code:Int,
    val message: String,
    val data: DataPassRst
    )
