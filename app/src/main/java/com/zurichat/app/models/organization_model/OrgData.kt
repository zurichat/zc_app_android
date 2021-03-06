package com.zurichat.app.models.organization_model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zurichat.app.ui.organizations.localdatabase.TypeConverters.StringListTypeConverter
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "OrgData")
data class OrgData(
    @PrimaryKey
    val id: String = "",
    @TypeConverters(StringListTypeConverter::class)
    var imgs: List<String>,
    val isOwner: Boolean = false,
    val logo_url: String = "",
    val member_id: String = "",
    val name: String = "",
    val no_of_members: Int = 0,
    val workspace_url: String = ""
) : Parcelable