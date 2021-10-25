package com.zurichat.app.data.remoteSource

import com.zurichat.app.models.*
import com.zurichat.app.models.network_response.*
import com.zurichat.app.models.organization_model.*
import com.zurichat.app.models.organization_model.OrganizationCreator
import com.zurichat.app.models.organization_model.OrganizationCreatorResponse
import com.zurichat.app.ui.login.password.confirm.ConfirmPasswordData
import com.zurichat.app.ui.login.password.confirm.ConfirmResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


//'https://api.zuri.chat/v1/auth/login'

interface UsersService {

    @POST("auth/login")
    suspend fun login(@Body loginBody: LoginBody): LoginResponse

    @POST("account/request-password-reset-code")
    suspend fun passwordreset(@Body passwordResetBody: PasswordResetBody): PasswordRestReponse

    @POST("organizations")
    suspend fun createOrganization(@Body organizationCreator: OrganizationCreator): OrganizationCreatorResponse

    @PATCH("organizations/{organization_id}/name")
    suspend fun updateOrganizationName(
        @Path("organization_id") orgId: String,
        @Body organization_name: OrganizationName
    ): OrganizationNameResponse

    @POST("users")
    fun register(@Body registerUser: RegisterUser?): Call<RegisterUser?>?


    @POST("account/verify-account")
    fun verifyEmail(@Body verifyEmail: VerifyEmail?): Call<VerifyEmail?>?

    @GET("organizations/{organization_id}/members")
    suspend fun getMembers(@Header("Authorization") token: String, @Path("organization_id") orgId: String): Response<UserList>

    @POST("account/request-password-reset-code")
    suspend fun passwordReset(@Body passwordResetBody: PasswordResetBody): Response<PasswordRestReponse>

    @GET("organizations/{organization_id}/members")
    suspend fun getMembers(@Path("organization_id") org_id: String): Response<OrganizationMembers>

    @GET("organizations/{organization_id}/members/{member_id}")
    suspend fun getMember(@Path("organization_id") org_id: String, @Path("member_id") member_id: String): Response<OrganizationMember>

    @GET("organizations/{organization_id}/members")
    suspend fun getMemberByEmail(
        @Path("organization_id") org_id: String,
        @Query("query") email: String
    ): Response<OrganizationMembers>

    @POST("auth/logout")
    suspend fun logout(@Body logoutBody: LogoutBody): Response<LogoutResponse>

    @POST("account/verify-reset-password")
    suspend fun verifyResetOtp(@Body resetCodeBody: ResetCodeBody): Response<ResetCodeResponse>

    @POST("account/update-password/{verification_code}")
    suspend fun updatePass(
        @Path("verification_code") verCode: String,
        @Body updatePassBody: UpdatePassBody
    ): Response<LogoutResponse>

    @POST("auth/confirm-password")
    suspend fun confirmpassword(@Body confirmpassword: ConfirmPasswordData): ConfirmResponse

    //DMService

    @GET("${DM_API}{org_id}/rooms/{room_id}/messages")
    suspend fun getMessages(
        @Path("org_id") orgId: String,
        @Path("room_id") roomId: String
    ): Response<GetMessagesResponse>


    @GET("dmgetmessage/{room_id}/{message_id}")
    suspend fun getMessage(
        @Path("room_id") roomId: String,
        @Path("message_id") messageId: String
    ): Response<Message>
    @POST("${DM_API}{org_id}/rooms/{room_id}/messages")
    suspend fun sendMessage(
        @Path("org_id") orgId: String,
        @Path("room_id") roomId: String,
        @Body message: Message
    ): Response<SendMessageResponse>

    @GET("${DM_API}{org_id}/users/{user_id}/rooms")
    suspend fun getRooms(
        @Path("org_id") orgId: String,
        @Path("user_id") userId: String
    ): Response<List<Room>>

    @GET("${DM_API}{org_id}/rooms/{room_id}/info")
    suspend fun getRoomInfo(
        @Path("org_id") orgId: String,
        @Query("room_id") roomId: String
    ): Response<RoomInfoResponse>

    @POST("${DM_API}{org_id}/users/{user_id}/room")
    suspend fun createRoom(
        @Path("org_id") orgId: String,
        @Path("user_id") userId: String,
        @Body roomInfo: CreateRoom
    ): Response<CreateRoomResponse>

    companion object {
        const val BASE_URL = "https://dm.zuri.chat/"
        const val DM_API = "api/v1/org/"
    }
}


