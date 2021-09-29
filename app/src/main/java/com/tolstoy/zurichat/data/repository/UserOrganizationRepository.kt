package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.functional.Result
import com.tolstoy.zurichat.models.organization_model.OrgRequestBody
import kotlinx.coroutines.flow.Flow

interface UserOrganizationRepository {
    fun getUserOrganization(orgRequestBody: OrgRequestBody): Flow<Result<Any>>
}