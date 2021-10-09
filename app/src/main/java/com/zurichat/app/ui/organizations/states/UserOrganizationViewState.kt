package com.zurichat.app.ui.organizations.states

import com.zurichat.app.models.organization_model.UserOrganizationModel

sealed class UserOrganizationViewState {
    class Success(
        val message: Int,
        val userOrganizationResponseModel: UserOrganizationModel? = null,
    ) : UserOrganizationViewState()

    class Failure(val message: String) : UserOrganizationViewState()
    class Loading(val message: Int? = null) : UserOrganizationViewState()
    object Empty : UserOrganizationViewState()
}
