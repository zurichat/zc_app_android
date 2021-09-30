package com.tolstoy.zurichat.data.repository


import com.tolstoy.zurichat.data.functional.Result
import com.tolstoy.zurichat.models.organization_model.OrgRequestBody
import com.tolstoy.zurichat.ui.organizations.UserOrganizationRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserOrganizationRepositoryImpl @Inject constructor(private val userOrganizationRemote: UserOrganizationRemote) :
    UserOrganizationRepository {
    override fun getUserOrganization(orgRequestBody: OrgRequestBody): Flow<Result<Any>> {
        return flow {
            when (val res =
                userOrganizationRemote.getUserOrganization(orgRequestBody)) {
                is Result.Success -> {
                    emit(Result.Success(res.data))
                }
                is Result.Failed -> {
                    emit(Result.Failed(res.errorMessage))
                }
            }
        }
    }
}
