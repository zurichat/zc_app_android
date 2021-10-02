package com.tolstoy.zurichat.ui.organizations.states

import com.tolstoy.zurichat.models.network_response.UserOrganizationModel
import com.tolstoy.zurichat.ui.profile.data.UserOrganizationResponse

sealed class UserOrganizationViewState {
    class Success(
        val message: Int,
        val userOrganizationResponseModel: UserOrganizationModel? = null,
    ) : UserOrganizationViewState()

    class Failure(val message: String) : UserOrganizationViewState()
    class Loading(val message: Int? = null) : UserOrganizationViewState()
    object Empty : UserOrganizationViewState()
}
