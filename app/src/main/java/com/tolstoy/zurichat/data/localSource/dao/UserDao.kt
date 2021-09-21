package com.tolstoy.zurichat.data.localSource.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tolstoy.zurichat.data.localSource.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUser(): Flow<UserEntity>

    @Query("SELECT * FROM user WHERE user.email LIKE:email")
    fun getUserData(email: String): UserEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserEntity)
}