package com.zurichat.app.models.organization_model

import com.zurichat.app.models.OrganizationMember

data class OrganizationListMember(
    val status : Int,
    val message : String,
    val data : List<OrganizationMember>
)
