package com.tolstoy.zurichat.data.localSource.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.tolstoy.zurichat.models.User

@Dao

interface AccountsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE currentUser = 0 ")
    fun readAllData(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE currentUser = 1")
    fun getUser(): LiveData<User?>
}