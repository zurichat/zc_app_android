package com.zurichat.app.models.organization_model

data class Data(
    val id: String,
    val imgs: List<String>,
    val isOwner: Boolean,
    val logo_url: String,
    val member_id: String,
    val name: String,
    val no_of_members: Int,
    val workspace_url: String
)