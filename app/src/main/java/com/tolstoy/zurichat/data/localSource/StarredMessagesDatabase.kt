package com.tolstoy.zurichat.data.localSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.tolstoy.zurichat.data.localSource.dao.StarredMessagesDao
import com.tolstoy.zurichat.models.StarredMessages
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [StarredMessages::class], version = 1)
abstract class StarredMessagesDatabase : RoomDatabase() {

    abstract fun StarredMessagesDao(): StarredMessagesDao

    /*private class StarredMessageDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var starredMessagesDao = database.StarredMessagesDao()

                    var starredMessages = StarredMessages("", "", "", "", "")
                    starredMessagesDao.starMessage(starredMessages)
                }
            }
        }
    }*/

    companion object {
        @Volatile
        private var INSTANCE: StarredMessagesDatabase? = null

        fun getDatabase(context: Context): StarredMessagesDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StarredMessagesDatabase::class.java,
                    "starred_messages_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}