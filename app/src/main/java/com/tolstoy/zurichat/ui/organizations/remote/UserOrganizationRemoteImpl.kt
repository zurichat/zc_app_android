package com.tolstoy.zurichat.ui.organizations.remote

import com.tolstoy.zurichat.data.functional.Failure
import com.tolstoy.zurichat.data.functional.Result
import com.tolstoy.zurichat.data.remoteSource.OrganizationService
import com.tolstoy.zurichat.util.handleErrorMessage
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

