package com.tolstoy.zurichat.data.localSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tolstoy.zurichat.models.OrganizationMember
import com.tolstoy.zurichat.models.User
import kotlinx.coroutines.flow.Flow

/**
 * The users table is acting as a local cache while the app
 * is still trying to get data from the remote server or
 * while the device is offline
 * */
@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<User>

    @Query("SELECT * FROM users WHERE id LIKE:id")
    suspend fun getUser(id: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(vararg user: User)

    @Query("SELECT * FROM members WHERE orgId LIKE:orgId")
    suspend fun getMembers(orgId: String): List<OrganizationMember>

    @Query("SELECT * FROM members WHERE orgId LIKE:orgId AND id LIKE:memberId")
    suspend fun getMember(orgId: String, memberId: String): OrganizationMember

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMember(vararg member: OrganizationMember)
}