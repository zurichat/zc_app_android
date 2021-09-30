package com.tolstoy.zurichat.ui.profile.data

import com.tolstoy.zurichat.models.organization_model.Data

data class UserOrganizationResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Int
)