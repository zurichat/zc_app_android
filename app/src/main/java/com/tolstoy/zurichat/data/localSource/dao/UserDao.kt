package com.tolstoy.zurichat.data.localSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tolstoy.zurichat.models.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE id LIKE:id")
    suspend fun getUser(id: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

}