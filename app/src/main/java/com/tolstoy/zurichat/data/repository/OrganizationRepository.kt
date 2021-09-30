package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.remoteSource.UsersService
import com.tolstoy.zurichat.models.organization_model.OrganizationCreator
import com.tolstoy.zurichat.models.organization_model.OrganizationCreatorResponse
import javax.inject.Inject

class OrganizationRepository @Inject constructor(
    private val usersService: UsersService
) {
    suspend fun getOrganization(organizationCreator: OrganizationCreator): OrganizationCreatorResponse{
        return usersService.createOrganization(organizationCreator)
    }
}