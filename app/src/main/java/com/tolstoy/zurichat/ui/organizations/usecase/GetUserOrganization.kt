package com.tolstoy.zurichat.ui.organizations.usecase

import com.chamsmobile.android.core.functional.base.FlowUseCase
import com.tolstoy.zurichat.data.functional.Result
import com.tolstoy.zurichat.data.repository.UserOrganizationRepository
import com.tolstoy.zurichat.models.organization_model.OrgRequestBody
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserOrganization @Inject constructor(
    private val userRepository: UserOrganizationRepository,
) : FlowUseCase<OrgRequestBody, Any> {
    override fun invoke(params: OrgRequestBody?): Flow<Result<Any>> {
        return userRepository.getUserOrganization(params!!)
    }
}
