package com.tolstoy.zurichat.models

import com.google.gson.annotations.SerializedName

data class UserList(
    @SerializedName("data")
    val data: List<OrganizationMember> )
