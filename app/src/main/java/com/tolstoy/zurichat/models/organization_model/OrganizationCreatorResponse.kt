package com.tolstoy.zurichat.models.organization_model

data class OrganizationCreatorResponse(
    val code: Int,
    val organizationCreatorIdData: OrganizationCreatorIdData,
    val message: String
)