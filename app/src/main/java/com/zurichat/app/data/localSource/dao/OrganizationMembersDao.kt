package com.zurichat.app.data.localSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zurichat.app.data.localSource.OrganizationMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrganizationMembersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembers(list: List<OrganizationMemberEntity>)

    @Query("SELECT * FROM organizationMembers WHERE orgId LIKE :orgId")
    fun getMembers(orgId: String): Flow<List<OrganizationMemberEntity>>

}