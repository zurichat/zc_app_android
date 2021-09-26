package com.tolstoy.zurichat.models.OrganizationModel

data class OrganizationCreatorResponse(
    val code: Int,
    val organizationCreatorIdData: OrganizationCreatorIdData,
    val message: String
)