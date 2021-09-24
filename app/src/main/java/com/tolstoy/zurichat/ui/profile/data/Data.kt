package com.tolstoy.zurichat.ui.profile.data

data class Data(
    val id: String,
    val isOwner: Boolean,
    val logo_url: String,
    val name: String,
    val no_of_members: Int,
    val workspace_url: String
)