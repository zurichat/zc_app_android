package com.zurichat.app.ui.profile.data

import com.zurichat.app.models.organization_model.OrgData

data class UserOrganizationResponse(
    val `data`: List<OrgData>,
    val message: String,
    val status: Int
)