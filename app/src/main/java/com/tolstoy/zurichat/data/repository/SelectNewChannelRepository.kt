package com.tolstoy.zurichat.data.repository

import android.util.Log
import com.tolstoy.zurichat.data.functional.Failure
import com.tolstoy.zurichat.data.functional.GetUserResult
import com.tolstoy.zurichat.data.localSource.dao.OrganizationMembersDao
import com.tolstoy.zurichat.data.remoteSource.UsersService
import com.tolstoy.zurichat.di.ChannelRetrofitService
import com.tolstoy.zurichat.models.OrganizationMember
import com.tolstoy.zurichat.models.UserList
import com.tolstoy.zurichat.util.mapToEntityList
import com.tolstoy.zurichat.util.mapToMemberList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SelectNewChannelRepository

@Inject constructor(@ChannelRetrofitService private val retrofitService: UsersService,
                    private val dao: OrganizationMembersDao
) {

    //Fetch list of users from the local database
    suspend fun getMembers(orgId: String) : Flow<GetUserResult<List<OrganizationMember>>> {
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

            if(res.isSuccessful) {
                res.body()?.let {
                    dao.insertMembers(it.data.mapToEntityList())
                    GetUserResult.Success(it)
                }?: GetUserResult.Error(Failure.ServerError)
            } else {
                GetUserResult.Error(Failure.ServerError)

            }
        }
        catch (excep: Exception) {
            GetUserResult.Error(Failure.ServerError)
        }
    }
}