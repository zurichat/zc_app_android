package com.zurichat.app.data.localSource

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zurichat.app.data.localSource.dao.OrganizationMembersDao
import com.zurichat.app.data.localSource.dao.UserDao
import com.zurichat.app.models.User
import com.zurichat.app.models.organization_model.OrgData
import com.zurichat.app.ui.fragments.channel_chat.localdatabase.ChannelMessagesDao
import com.zurichat.app.ui.fragments.channel_chat.localdatabase.RoomDao
import com.zurichat.app.ui.fragments.channel_chat.localdatabase.RoomDataObject
import com.zurichat.app.ui.fragments.channel_chat.localdatabase.TypeConverters.DataTypeConverter
import com.zurichat.app.ui.fragments.home_screen.chats_and_channels.localdatabase.AllChannelListDao
import com.zurichat.app.ui.fragments.home_screen.chats_and_channels.localdatabase.AllChannelListObject
import com.zurichat.app.ui.fragments.home_screen.chats_and_channels.localdatabase.ChannelListDao
import com.zurichat.app.ui.fragments.home_screen.chats_and_channels.localdatabase.ChannelListObject
import com.zurichat.app.ui.fragments.home_screen.chats_and_channels.localdatabase.TypeConverter.ChannelListConverter
import com.zurichat.app.ui.fragments.model.AllChannelMessages
import com.zurichat.app.ui.organizations.localdatabase.OrgDao
import com.zurichat.app.ui.organizations.localdatabase.OrgRoomData
import com.zurichat.app.ui.organizations.localdatabase.TypeConverters.OrgDataTypeConverter
import com.zurichat.app.ui.organizations.localdatabase.TypeConverters.StringListTypeConverter

@Database(
    entities = [User::class, OrganizationMemberEntity::class, RoomDataObject::class, AllChannelMessages::class, ChannelListObject::class, AllChannelListObject::class, OrgData::class, OrgRoomData::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(DataTypeConverter::class, ChannelListConverter::class, StringListTypeConverter::class, OrgDataTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun organizationMembersDao(): OrganizationMembersDao

    abstract fun roomDao(): RoomDao

    abstract fun channelMessagesDao(): ChannelMessagesDao

    abstract fun channelListDao(): ChannelListDao

    abstract fun allChannelListDao(): AllChannelListDao

    abstract fun orgDao(): OrgDao
}
