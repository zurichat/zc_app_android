package com.zurichat.app.ui.organizations.remote

import com.zurichat.app.data.functional.Result

interface UserOrganizationRemote {
    suspend fun getUserOrganization(emailAddress:String): Result<Any>
}