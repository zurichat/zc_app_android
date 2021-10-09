package com.zurichat.app.models.organization_model

data class Organization(
    val organizationData: OrganizationData,
    val message: String,
    val status: Int
)