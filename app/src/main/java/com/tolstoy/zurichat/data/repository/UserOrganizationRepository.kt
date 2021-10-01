package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.functional.Result
import kotlinx.coroutines.flow.Flow

interface UserOrganizationRepository {
    fun getUserOrganization(emailAddress:String): Flow<Result<Any>>
}