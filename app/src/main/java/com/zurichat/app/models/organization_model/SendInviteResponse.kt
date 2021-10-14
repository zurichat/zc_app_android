package com.zurichat.app.models.organization_model

import com.zurichat.app.models.Data

data class SendInviteResponse(
    val status: String,
    val message: String,
    val `data`: Data
)