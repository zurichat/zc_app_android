package com.tolstoy.zurichat.models.organization_model

data class OrganizationData(
    val _id: String,
    val admins: Any,
    val created_at: String,
    val creator_email: String,
    val creator_id: String,
    val logo_url: String,
    val name: String,
    val plugins: Any,
    val settings: Any,
    val updated_at: String,
    val workspace_url: String
)