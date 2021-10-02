package com.tolstoy.zurichat.ui.fragments.channel_chat

import com.tolstoy.zurichat.models.OrganizationMember

data class UserOrganizationMemberResponse(
    val code: Int,
    val `data`: OrganizationMember,
    val message: String
)