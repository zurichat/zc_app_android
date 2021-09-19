package com.tolstoy.zurichat.data.localSource

import android.content.Context
import android.service.autofill.UserData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tolstoy.zurichat.data.localSource.dao.UserDao
import com.tolstoy.zurichat.data.localSource.entities.UserEntity

@Database(entities = [UserEntity::class], version = 1)
 abstract class AppDatabase: RoomDatabase() {

     abstract fun userDao(): UserDao

     companion object {
         @Volatile
         private var instance: AppDatabase? = null
         private val LOCK = Any()

         operator fun invoke(context: Context) = instance?: synchronized(LOCK) {
             instance ?: buildDatabase(context)
         }
         private fun buildDatabase(context: Context) = Room.databaseBuilder(
             context, AppDatabase::class.java, "user.db").allowMainThreadQueries().build()
     }


}