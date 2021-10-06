package com.zurichat.app.ui.profile.data

import com.zurichat.app.models.organization_model.Data

data class UserOrganizationResponse(
    val `data`: List<Data>,
    val message: String,
    val status: Int
)