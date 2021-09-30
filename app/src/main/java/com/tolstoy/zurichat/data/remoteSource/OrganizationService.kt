package com.tolstoy.zurichat.data.remoteSource

import com.tolstoy.zurichat.models.Message
import com.tolstoy.zurichat.models.network_response.*
import com.tolstoy.zurichat.models.organization_model.UserOrganizationModel
import retrofit2.Response
import retrofit2.http.*

/**
 * @author Richard Ebo [https://github.com/solidtm]
 * Created 29-Oct-21
 */
//https://api.zuri.chat/users/glagoandrew2001@gmail.com/organizations
interface OrganizationService {
    @GET("users/{email_address}/organizations")
    suspend fun getUserOrganizations(
        @Path("email_address") emailAddress: String): Response<UserOrganizationModel>
}
