package com.zurichat.app.data.repository


import com.zurichat.app.data.functional.Result
import com.zurichat.app.ui.organizations.remote.UserOrganizationRemote
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserOrganizationRepositoryImpl @Inject constructor(private val userOrganizationRemote: UserOrganizationRemote) :
    UserOrganizationRepository {
    override fun getUserOrganization(emailAddress: String): Flow<Result<Any>> {
        return flow {
            when (val res =
                userOrganizationRemote.getUserOrganization(emailAddress = emailAddress)) {
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
