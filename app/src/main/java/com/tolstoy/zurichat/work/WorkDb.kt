package com.tolstoy.zurichat.work

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tolstoy.zurichat.data.localSource.dao.UserDao

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class WorkDb: RoomDatabase() {

        abstract fun dao(): UserDao

        companion object {

            @Volatile
            private var INSTANCE: WorkDb? = null

            fun getDatabase(context: Context): WorkDb {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder( context.applicationContext, WorkDb::class.java, "app_database")
                        .build()
                    INSTANCE = instance
                    instance
                }
            }

    }
}