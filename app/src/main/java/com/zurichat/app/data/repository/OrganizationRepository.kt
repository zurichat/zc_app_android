package com.zurichat.app.data.repository

import android.content.SharedPreferences
import com.zurichat.app.data.remoteSource.UsersService
import com.zurichat.app.data.remoteSource.enqueue
import com.zurichat.app.models.organization_model.OrganizationCreator
import com.zurichat.app.models.organization_model.OrganizationCreatorResponse
import com.zurichat.app.models.organization_model.OrganizationName
import com.zurichat.app.models.organization_model.OrganizationNameResponse
import com.zurichat.app.util.ORG_ID
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class OrganizationRepository @Inject constructor(private val usersService: UsersService, private val preferences: SharedPreferences) {
    suspend fun getOrganization(organizationCreator: OrganizationCreator): OrganizationCreatorResponse {
        return usersService.createOrganization(organizationCreator)
    }

    suspend fun updateOrganizationName(orgID: String, orgName: String): OrganizationNameResponse {
        val organizationNameResponse = OrganizationName(orgName)
        return usersService.updateOrganizationName(orgID,organizationNameResponse)
    }

    @ExperimentalCoroutinesApi
    suspend fun getMember(userId: String, organizationId: String = getId()) = usersService.getMember(organizationId, userId).enqueue()

    @ExperimentalCoroutinesApi
    suspend fun getMembers(organizationId: String = getId()) = usersService.getMembers(organizationId).enqueue()

    fun saveId(id: String) = preferences.edit().putString(ORG_ID, id).apply()

    fun getId(): String {
        preferences.getString(ORG_ID, "")
        return TEST_ID
    }

    companion object {
        val TAG = OrganizationRepository::class.simpleName

        const val TEST_ID = "6145eee9285e4a18402074cd" // used for testing if endpoints are working
    }
}