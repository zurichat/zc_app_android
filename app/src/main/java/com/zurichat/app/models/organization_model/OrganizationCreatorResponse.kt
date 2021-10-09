package com.zurichat.app.models.organization_model


data class OrganizationCreatorResponse(
    val code: Int,
    val data: OrganizationCreatorIdData,
    val message: String
)