package com.tolstoy.zurichat.ui.profile.data

data class UserMemberResponse(
    val `data`: List<DataX>,
    val message: String,
    val status: Int
)