package com.tolstoy.zurichat.ui.organizations.remote

import com.tolstoy.zurichat.data.functional.Result

interface UserOrganizationRemote {
    suspend fun getUserOrganization(emailAddress:String): Result<Any>
}