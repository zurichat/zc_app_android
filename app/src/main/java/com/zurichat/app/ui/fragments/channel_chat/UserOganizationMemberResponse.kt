package com.zurichat.app.ui.fragments.channel_chat

import com.zurichat.app.models.OrganizationMember

data class UserOrganizationMemberResponse(
    val code: Int,
    val `data`: OrganizationMember,
    val message: String
)