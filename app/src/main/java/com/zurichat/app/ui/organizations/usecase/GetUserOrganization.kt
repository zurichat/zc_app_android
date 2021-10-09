package com.zurichat.app.ui.organizations.usecase

import com.chamsmobile.android.core.functional.base.FlowUseCase
import com.zurichat.app.data.functional.Result
import com.zurichat.app.data.repository.UserOrganizationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserOrganization @Inject constructor(
    private val userRepository: UserOrganizationRepository,
) : FlowUseCase<String, Any> {
    override fun invoke(params: String?): Flow<Result<Any>> {
        return userRepository.getUserOrganization(params!!)
    }
}
