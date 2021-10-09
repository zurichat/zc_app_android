package com.zurichat.app.ui.organizations.remote

import com.zurichat.app.data.functional.Failure
import com.zurichat.app.data.functional.Result
import com.zurichat.app.data.remoteSource.OrganizationService
import com.zurichat.app.util.handleErrorMessage
import javax.inject.Inject

class UserOrganizationRemoteImpl @Inject constructor(
    private val apiService: OrganizationService
) : UserOrganizationRemote {

    override suspend fun getUserOrganization(emailAddress:String): Result<Any> {
        return try {
            val res = apiService.getUserOrganizations(emailAddress = emailAddress)
            when (res.isSuccessful) {
                true -> {
                    res.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error(Failure.ServerError)
                }
                false -> {
                    val errorMessage = handleErrorMessage(res.errorBody()!!)
                    Result.Failed(errorMessage)
                }
            }
        } catch (exc: Exception) {
            println(exc)
            Result.Failed("Try Again")
        }
    }
}

