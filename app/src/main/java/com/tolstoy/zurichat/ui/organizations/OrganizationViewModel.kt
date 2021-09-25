package com.tolstoy.zurichat.ui.organizations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tolstoy.zurichat.data.repository.OrganizationRepository
import com.tolstoy.zurichat.models.OrganizationModel.OrganizationCreator
import com.tolstoy.zurichat.models.OrganizationModel.OrganizationCreatorResponse
import com.tolstoy.zurichat.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OrganizationViewModel @Inject constructor(
    private val organizationRepository: OrganizationRepository
) : ViewModel() {

    private val _organizationCreator = MutableLiveData<Result<OrganizationCreatorResponse>>()
    val organizationCreator : LiveData<Result<OrganizationCreatorResponse>> = _organizationCreator


    private val exceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
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
}