package com.zurichat.app.data.localSource.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zurichat.app.models.User

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

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users WHERE email LIKE :email")
    fun getUser(email: String): User

}