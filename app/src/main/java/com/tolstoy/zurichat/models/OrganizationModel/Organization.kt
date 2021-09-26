package com.tolstoy.zurichat.models.OrganizationModel

data class Organization(
    val organizationData: OrganizationData,
    val message: String,
    val status: Int
)