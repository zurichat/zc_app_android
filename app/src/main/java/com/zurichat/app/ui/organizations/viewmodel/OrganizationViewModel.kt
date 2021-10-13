package com.zurichat.app.ui.organizations.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zurichat.app.data.repository.OrganizationRepository
import com.zurichat.app.models.organization_model.OrganizationCreator
import com.zurichat.app.models.organization_model.OrganizationCreatorResponse
import com.zurichat.app.models.organization_model.OrganizationNameResponse
import com.zurichat.app.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrganizationViewModel @Inject constructor(private val organizationRepository: OrganizationRepository) : ViewModel() {

    private val _organizationCreator = MutableLiveData<Result<OrganizationCreatorResponse>>()
    val organizationCreator : LiveData<Result<OrganizationCreatorResponse>> = _organizationCreator

    private val _organizationName = MutableLiveData<Result<OrganizationNameResponse>>()
    val organizationName : LiveData<Result<OrganizationNameResponse>> = _organizationName

    private val exceptionHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable)
            _organizationCreator.postValue(Result.Error(throwable))
        }

    fun createOrganization(organizationCreator: OrganizationCreator) {
        _organizationCreator.postValue(Result.Loading)
        viewModelScope.launch(exceptionHandler) {
            val organizationCreatorResponse = organizationRepository.getOrganization(organizationCreator)
            _organizationCreator.postValue(Result.Success(organizationCreatorResponse))
        }
    }

    fun updateOrganizationName(orgID: String, orgName: String) {
        _organizationName.postValue(Result.Loading)
        viewModelScope.launch(exceptionHandler) {
            val organizationNameResponse = organizationRepository.updateOrganizationName(orgID, orgName)
            _organizationName.postValue(Result.Success(organizationNameResponse))
        }
    }
}