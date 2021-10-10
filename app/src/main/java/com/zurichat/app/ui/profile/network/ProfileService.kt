package com.zurichat.app.ui.profile.network

import com.zurichat.app.ui.profile.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProfileService {


    //phone number update service
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("organizations/{id}/members/{mem_id}/profile")
    fun updateProfilePhone(
        @Path("id") id: String,
        @Path("mem_id") mem_id: String,
        @Body profilePayload: PhoneUpdate
    ): Call<ProfileResponse>

    // name update service
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("organizations/{id}/members/{mem_id}/profile")
    fun updateProfileName(
        @Path("id") id: String,
        @Path("mem_id") mem_id: String,
        @Body profilePayload: NameUpdate
    ): Call<ProfileResponse>

    //about or description update service
    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("organizations/{id}/members/{mem_id}/profile")
    fun updateProfileBio(
        @Path("id") id: String,
        @Path("mem_id") mem_id: String,
        @Body profilePayload: AboutUpdate
    ): Call<ProfileResponse>

    //update profile photo service

    @Headers("Content-Type: application/json;charset=UTF-8")
    @Multipart
    @PATCH("organizations/{id}/members/{mem_id}/photo")
    fun updatePhoto(
        @Path("id") id: String,
        @Path("mem_id") mem_id: String,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ProfilePhotoResponse>

    //get organization Id from list of organizations
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("users/{email}/organizations")
    fun getUserOrg(
        @Path("email") email: String?
    ): Call<UserOrganizationResponse>

    //get member id from list of members in an organization
    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("organizations/{orgId}/members")
    fun getUserMemberId(
        @Path("orgId") orgId: String
    ): Call<UserMemberResponse>
}