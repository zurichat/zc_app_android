package com.tolstoy.zurichat.ui.profile.data

import com.tolstoy.zurichat.models.network_response.UserOrganizationModel


data class UserOrganizationResponse(
    val `data`: List<UserOrganizationModel.Data>,
    val message: String,
    val status: Int
)