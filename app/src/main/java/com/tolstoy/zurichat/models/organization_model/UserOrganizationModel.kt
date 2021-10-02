package com.tolstoy.zurichat.models.organization_model

data class UserOrganizationModel(
    val `data`: List<Data>,
    val message: String,
    val status: Int
)