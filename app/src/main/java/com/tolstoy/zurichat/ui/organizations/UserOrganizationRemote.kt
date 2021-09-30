package com.tolstoy.zurichat.ui.organizations

import com.tolstoy.zurichat.data.functional.Result
import com.tolstoy.zurichat.models.organization_model.OrgRequestBody

interface UserOrganizationRemote {
    suspend fun getUserOrganization(orgRequestBody: OrgRequestBody): Result<Any>
}