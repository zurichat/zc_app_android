package com.tolstoy.zurichat.ui.profile.data

data class UserOrganizationResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Int
)