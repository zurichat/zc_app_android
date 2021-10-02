package com.tolstoy.zurichat.data.localSource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tolstoy.zurichat.data.localSource.dao.OrganizationMembersDao
import com.tolstoy.zurichat.data.localSource.dao.UserDao
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase.ChannelMessagesDao
import com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase.RoomDao
import com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase.RoomDataObject
import com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase.TypeConverters.DataTypeConverter
import com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels.localdatabase.ChannelListDao
import com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels.localdatabase.TypeConverter.ChannelConverter
import com.tolstoy.zurichat.ui.fragments.model.AllChannelMessages

@Database(entities = [User::class, OrganizationMemberEntity::class, RoomDataObject::class, AllChannelMessages::class,ChannelModel::class], version = 3, exportSchema = false)
@TypeConverters(DataTypeConverter::class,ChannelConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun organizationMembersDao(): OrganizationMembersDao

    abstract fun roomDao(): RoomDao

    abstract fun channelMessagesDao(): ChannelMessagesDao

    abstract fun channelListDao(): ChannelListDao
}
