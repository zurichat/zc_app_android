package com.tolstoy.zurichat.models.organization_model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class OrganizationCreatorResponse(
    val code: Int,
    val data: OrganizationCreatorIdData,
    val message: String
)