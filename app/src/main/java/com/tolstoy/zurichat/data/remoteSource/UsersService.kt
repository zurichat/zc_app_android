package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.*
import com.tolstoy.zurichat.models.network_response.OrganizationCreator
import com.tolstoy.zurichat.models.network_response.OrganizationCreatorResponse
import com.tolstoy.zurichat.models.network_response.OrganizationMembers
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


//'https://api.zuri.chat/v1/auth/login'

interface UsersService {

    @POST("auth/login")
    suspend fun login(@Body loginBody: LoginBody): LoginResponse

    @POST("account/request-password-reset-code")
    suspend fun passwordReset(@Body passwordReset: PasswordReset): PassswordRestReponse

    @POST("organizations")
    suspend fun createOrganization(@Body organizationCreator: OrganizationCreator): OrganizationCreatorResponse

    @POST("users")
    fun register(@Body registerUser: RegisterUser?): Call<RegisterUser?>?

    @POST ("account/verify-account")
    fun verifyEmail(@Body verifyEmail : VerifyEmail?): Call<VerifyEmail?>?

    @GET("organizations/{organization_id}/members")
    fun getMembers(@Path("organization_id") org_id: String): Call<OrganizationMembers>

    @GET("organizations/{organization_id}/members/{member_id}")
    fun getMember(@Path("organization_id") org_id: String,
                          @Path("member_id") member_id: String): Call<OrganizationMember>
}


