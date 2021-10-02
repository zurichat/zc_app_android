package com.tolstoy.zurichat.data.localSource.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tolstoy.zurichat.models.User
/**
 * The users table is acting as a local cache while the app
 * is still trying to get data from the remote server or
 * while the device is offline
 * */
@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    suspend fun getUsers(): List<User>

    // if you don't understand this query, sqlite stores booleans as integers [false = 0, true = 1]
    // so this query returns the current user as the currentUser boolean field is updated to
    // true in the login fragment
    /**
     * @return the current user
     */
    @Query("SELECT * FROM users WHERE currentUser = 1")
    suspend fun getUser(): User

    @Query("SELECT * FROM users WHERE email LIKE:email")
    suspend fun getUser(email: String): User

    @Query("SELECT * FROM users WHERE id LIKE:id")
    suspend fun getUserById(id: String): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: User)

}