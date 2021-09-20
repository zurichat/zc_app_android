package com.tolstoy.zurichat.ui.profile.network

import com.tolstoy.zurichat.ui.profile.data.ProfilePayload
import com.tolstoy.zurichat.ui.profile.data.ProfileResponse
import com.tolstoy.zurichat.ui.profile.data.UserOrganizationResponse
import retrofit2.Call
import retrofit2.http.*

interface ProfileService {


    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("organizations/{id}/members/{mem_id}/profile")
    fun updateProfile(
        @Path("id") id: String,
        @Path("mem_id") mem_id: String,
        @Body profilePayload: ProfilePayload
    ): Call<ProfileResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @PATCH("organizations/{id}/members/{mem_id}/photo")
    fun updatePhoto(
        @Path("id") id: String,
        @Path("mem_id") mem_id: String,
        @Body profilePayload: ProfilePayload
    ): Call<ProfileResponse>

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("users/{email}/organizations")
    fun getUserOrg(
        @Path("email") email: String
    ): Call<UserOrganizationResponse>
}