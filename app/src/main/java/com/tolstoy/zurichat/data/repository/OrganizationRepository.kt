package com.tolstoy.zurichat.data.repository

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tolstoy.zurichat.data.localSource.dao.UserDao
import com.tolstoy.zurichat.data.remoteSource.UsersService
import com.tolstoy.zurichat.data.remoteSource.enqueue
import com.tolstoy.zurichat.models.OrganizationMember
import com.tolstoy.zurichat.models.network_response.OrganizationCreator
import com.tolstoy.zurichat.models.network_response.OrganizationCreatorResponse
import com.tolstoy.zurichat.models.network_response.OrganizationMembers
import com.tolstoy.zurichat.util.ORG_ID
import com.tolstoy.zurichat.util.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrganizationRepository @Inject constructor(
    private val usersService: UsersService,
    private val usersDao: UserDao,
    private val preferences: SharedPreferences
) {

    suspend fun getOrganization(organizationCreator: OrganizationCreator): OrganizationCreatorResponse {
        return usersService.createOrganization(organizationCreator)
    }

    @ExperimentalCoroutinesApi
    suspend fun getMember(userId: String, organizationId: String = getId()) = flow {
        emit(
            Result.Success(usersDao.getMember(organizationId, userId))
        )

        usersService.getMember(organizationId, userId).enqueue().also {
            when(it){
                is Result.Success -> if(it.data.isSuccessful) it.data.body()?.let { emit(Result.Success(it)) }
                is Result.Failure -> emit(it)
            }
        }
    }

    @ExperimentalCoroutinesApi
    suspend fun getMembers(organizationId: String = getId()) = flow {
        emit(
            Result.Success(OrganizationMembers(200, "From Db",
                usersDao.getMembers(organizationId)))
        )

        usersService.getMembers(organizationId).enqueue().also {
            when(it){
                is Result.Success -> if(it.data.isSuccessful) it.data.body()?.let { emit(Result.Success(it)) }
                is Result.Failure -> emit(it)
            }
        }
    }

    suspend fun saveMembers(vararg member: OrganizationMember) = usersDao.saveMember(*member)

    fun saveId(id: String) = preferences.edit().putString(ORG_ID, id).apply()
    fun getId() = preferences.getString(ORG_ID, "")!!

    companion object {
        val TAG = OrganizationRepository::class.simpleName
    }
}