package com.zurichat.app.data.repository

import android.content.SharedPreferences
import com.zurichat.app.data.remoteSource.UsersService
import com.zurichat.app.data.remoteSource.result
import com.zurichat.app.models.organization_model.*
import com.zurichat.app.util.MEMBER_ID
import com.zurichat.app.util.ORG_ID
import javax.inject.Inject

class OrganizationRepository @Inject constructor(
    private val usersService: UsersService,
    private val preferences: SharedPreferences
) {
    suspend fun getOrganization(organizationCreator: OrganizationCreator): OrganizationCreatorResponse {
        return usersService.createOrganization(organizationCreator)
    }

    suspend fun updateOrganizationName(orgID: String, orgName: String): OrganizationNameResponse {
        val organizationNameResponse = OrganizationName(orgName)
        return usersService.updateOrganizationName(orgID,organizationNameResponse)
    }

    suspend fun getMember(userId: String, organizationId: String = getId()) =
        usersService.getMember(organizationId, userId).result()

    suspend fun getMemberByEmail(email: String, organizationId: String = getId()) =
        usersService.getMemberByEmail(organizationId, email).result()

    suspend fun getMembers(organizationId: String = getId()) =
        usersService.getMembers(organizationId).result()

    fun save(orgData: OrgData) = with(orgData){
        preferences.edit()
            .putString(ORG_ID, id)
            .putString(MEMBER_ID, member_id).apply()
    }

    fun getId(): String {
        preferences.getString(ORG_ID, "")
        return TEST_ID
    }

    fun getMemberId() = preferences.getString(MEMBER_ID, "")!!

    companion object {
        val TAG = OrganizationRepository::class.simpleName

        const val TEST_ID = "6162210d8e856323d6f12110" // used for testing if endpoints are working
    }
}