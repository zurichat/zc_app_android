package com.tolstoy.zurichat.ui.profile.network

import com.tolstoy.zurichat.ui.profile.data.ProfilePayload
import com.tolstoy.zurichat.ui.profile.data.ProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProfileService {


    @PATCH("organizations/{id}/members/{mem_id}/profile")
    fun updateProfile(
        @Header("Authorization") authToken: String?,
        @Path("id") id: String,
        @Path("mem_id") mem_id: String,
        @Body profilePayload: ProfilePayload
    ): Call<ProfileResponse>

    @PATCH("organizations/{id}/members/{mem_id}/photo")
    fun updatePhoto(
        @Header("Authorization") authToken: String?,
        @Path("id") id: String,
        @Path("mem_id") mem_id: String,
        @Body profilePayload: ProfilePayload
    ): Call<ProfileResponse>
}