package com.zurichat.app.data.localSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zurichat.app.data.localSource.dao.AccountsDao
import com.zurichat.app.models.User

@Database(entities = [User::class], version = 1)
abstract class AccountsDatabase : RoomDatabase() {

    abstract fun AccountsDao(): AccountsDao

    companion object {
        @Volatile
        private var INSTANCE: AccountsDatabase? = null

        fun getDatabase(context: Context): AccountsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccountsDatabase::class.java,
                    "accounts_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}