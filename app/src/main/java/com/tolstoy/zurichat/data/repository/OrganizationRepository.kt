package com.tolstoy.zurichat.data.repository

import com.tolstoy.zurichat.data.remoteSource.RetrofitService
import com.tolstoy.zurichat.models.OrganizationModel.OrganizationCreator
import com.tolstoy.zurichat.models.OrganizationModel.OrganizationCreatorResponse
import javax.inject.Inject

class OrganizationRepository @Inject constructor(
    private val retrofitService: RetrofitService
) {
    suspend fun getOrganization(organizationCreator: OrganizationCreator): OrganizationCreatorResponse{
        return retrofitService.createOrganization(organizationCreator)
    }
}