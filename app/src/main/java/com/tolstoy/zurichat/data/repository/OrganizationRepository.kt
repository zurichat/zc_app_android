package com.tolstoy.zurichat.data.repository

import android.content.SharedPreferences
import com.tolstoy.zurichat.data.remoteSource.UsersService
import com.tolstoy.zurichat.models.organization_model.OrganizationCreator
import com.tolstoy.zurichat.models.organization_model.OrganizationCreatorResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class OrganizationRepository @Inject constructor(
    private val usersService: UsersService,
    private val preferences: SharedPreferences
) {
    suspend fun getOrganization(organizationCreator: OrganizationCreator): OrganizationCreatorResponse{
        return usersService.createOrganization(organizationCreator)
    }

    @ExperimentalCoroutinesApi
    suspend fun getMember(userId: String, organizationId: String = getId()) =
        usersService.getMember(organizationId, userId).enqueue()

    @ExperimentalCoroutinesApi
    suspend fun getMembers(organizationId: String = getId()) =
        usersService.getMembers(organizationId).enqueue()

    fun saveId(id: String) = preferences.edit().putString(ORG_ID, id).apply()

    fun getId(): String{
        preferences.getString(ORG_ID, "")!!
        return TEST_ID
    }

    companion object {
        val TAG = OrganizationRepository::class.simpleName

        const val TEST_ID = "6145eee9285e4a18402074cd" // used for testing if endpoints are working
    }
}