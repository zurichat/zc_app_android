package com.tolstoy.zurichat.data.localSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tolstoy.zurichat.data.localSource.dao.AccountsDao
import com.tolstoy.zurichat.data.localSource.dao.UserDao
import com.tolstoy.zurichat.models.User

@Database(entities = [User::class], version = 1)
abstract class AccountsDatabase:RoomDatabase() {

    abstract fun AccountsDao():AccountsDao

    companion object{
        @Volatile
        private var INSTANCE: AccountsDatabase? = null

        fun getDatabase(context:Context):AccountsDatabase{
            val tempInstance = INSTANCE
            if (tempInstance!= null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccountsDatabase::class.java,
                    "accounts_database"
                ).build()
                INSTANCE= instance
                return instance
            }
        }
    }
}