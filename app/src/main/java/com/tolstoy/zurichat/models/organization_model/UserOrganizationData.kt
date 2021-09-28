package com.tolstoy.zurichat.models.organization_model

data class UserOrganizationData (
    val _id: String,
    val imgs: ArrayList<String>,
    val isOwner: Boolean,
    val logo_url: String,
    val name: String,
    val no_of_members: Int,
    val workspace_url: String
)