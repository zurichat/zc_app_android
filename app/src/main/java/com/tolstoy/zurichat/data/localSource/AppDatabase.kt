package com.tolstoy.zurichat.data.localSource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tolstoy.zurichat.data.localSource.dao.OrganizationMembersDao
import com.tolstoy.zurichat.data.localSource.dao.UserDao
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase.ChannelMessagesDao
import com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase.RoomDao
import com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase.RoomDataObject
import com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase.TypeConverters.DataTypeConverter
import com.tolstoy.zurichat.ui.fragments.model.AllChannelMessages

@Database(entities = [User::class, OrganizationMemberEntity::class, RoomDataObject::class, AllChannelMessages::class], version = 3, exportSchema = false)
@TypeConverters(DataTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun organizationMembersDao(): OrganizationMembersDao

    abstract fun roomDao(): RoomDao

    abstract fun channelMessagesDao(): ChannelMessagesDao
}
