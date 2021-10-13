package com.zurichat.app.models

data class UserList(
    val status: Int,
    val message: String,
    val data: List<OrganizationMember>
)
