package com.zurichat.app.data.repository

import com.zurichat.app.data.functional.Result
import kotlinx.coroutines.flow.Flow

interface UserOrganizationRepository {
    fun getUserOrganization(emailAddress: String): Flow<Result<Any>>
}