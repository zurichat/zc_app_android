package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.*
import com.tolstoy.zurichat.models.organization_model.OrganizationCreator
import com.tolstoy.zurichat.models.organization_model.OrganizationCreatorResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


//'https://api.zuri.chat/v1/auth/login'

interface UsersService {

    @POST("auth/login")
    suspend fun login(@Body loginBody: LoginBody): LoginResponse

    @POST("account/request-password-reset-code")
    suspend fun passwordreset(@Body passwordReset: PasswordReset): PassswordRestReponse

    @POST("organizations")
    suspend fun createOrganization(@Body organizationCreator: OrganizationCreator): OrganizationCreatorResponse

    @POST("users")
    fun register(@Body registerUser: RegisterUser?): Call<RegisterUser?>?

    @POST ("account/verify-account")
    fun verifyEmail(@Body verifyEmail : VerifyEmail?): Call<VerifyEmail?>?

    /**
     * The endpoint for fetching users has been blocked by the backend guys
     */
    @GET("users")
    suspend fun getUsers(@Header("Authorization")token: String): Response<UserList>

    @GET("organizations/{organization_id}/members")
    suspend fun getMembers(@Header("Authorization")token: String,
                           @Path("organization_id") orgId: String): Response<UserList>

}


