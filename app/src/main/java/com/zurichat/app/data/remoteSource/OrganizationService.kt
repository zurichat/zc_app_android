package com.zurichat.app.data.remoteSource

import com.zurichat.app.models.organization_model.UserOrganizationModel
import com.zurichat.app.ui.fragments.channel_chat.UserOrganizationMemberResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @author Richard Ebo [https://github.com/solidtm]
 * Created 29-Oct-21
 */
//https://api.zuri.chat/users/glagoandrew2001@gmail.com/organizations
interface OrganizationService {
    @GET("users/{email_address}/organizations")
    suspend fun getUserOrganizations(
        @Path("email_address") emailAddress: String
    ): Response<UserOrganizationModel>

    @GET("organizations/{organization_id}/members/{member_id}")
    suspend fun getOrganizationMember(
        @Path("organization_id") organizationId: String?,
        @Path("member_id") memberId: String?
    ): Response<UserOrganizationMemberResponse>
}
