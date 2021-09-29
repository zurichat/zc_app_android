package com.tolstoy.zurichat.data.localSource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tolstoy.zurichat.data.localSource.dao.UserDao
import com.tolstoy.zurichat.models.User

@Database(entities = [User::class], version = 1, exportSchema = false)
 abstract class AppDatabase: RoomDatabase() {

     abstract fun userDao(): UserDao
}
