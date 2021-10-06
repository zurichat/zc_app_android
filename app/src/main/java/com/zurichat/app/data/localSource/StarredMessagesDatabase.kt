package com.zurichat.app.data.localSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zurichat.app.data.localSource.dao.StarredMessagesDao
import com.zurichat.app.models.StarredMessages

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