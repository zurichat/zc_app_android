package com.zurichat.app.data.localSource.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zurichat.app.models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id LIKE:id")
    suspend fun getUser(id: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

    @Query("SELECT * FROM users")
    fun readAllData(): LiveData<List<User>>

}