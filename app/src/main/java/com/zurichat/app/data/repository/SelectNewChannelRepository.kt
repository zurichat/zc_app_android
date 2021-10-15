package com.zurichat.app.data.repository

import com.zurichat.app.data.functional.Failure
import com.zurichat.app.data.functional.GetUserResult
import com.zurichat.app.data.localSource.dao.OrganizationMembersDao
import com.zurichat.app.data.remoteSource.UsersService
import com.zurichat.app.di.ChannelRetrofitService
import com.zurichat.app.models.OrganizationMember
import com.zurichat.app.models.UserList
import com.zurichat.app.util.mapToEntityList
import com.zurichat.app.util.mapToMemberList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SelectNewChannelRepository
@Inject constructor(@ChannelRetrofitService private val retrofitService: UsersService, private val dao: OrganizationMembersDao) {
    //Fetch list of users from the local database
    suspend fun getMembers(orgId: String): Flow<GetUserResult<List<OrganizationMember>>> {
        return try {
            flow<GetUserResult<List<OrganizationMember>>> {
                dao.getMembers(orgId).collect {
                    emit(GetUserResult.Success(it.mapToMemberList()))
                }
            }
        } catch (excep: Exception) {
            flow<GetUserResult<List<OrganizationMember>>> {
                emit(GetUserResult.Error(Failure.DataError))
            }
        }
    }

    //Fetches list of users from the endpoint and save them in the local database
    suspend fun insertUsers(token: String, orgId: String): GetUserResult<UserList> {
        val res = retrofitService.getMembers(token, orgId)
        return try {
            if (res.isSuccessful) {
                res.body()?.let {
                    dao.insertMembers(it.data.mapToEntityList())
                    GetUserResult.Success(it)
                } ?: GetUserResult.Error(Failure.ServerError)
            } else {
                GetUserResult.Error(Failure.ServerError)
            }
        } catch (excep: Exception) {
            GetUserResult.Error(Failure.ServerError)
        }
    }
}