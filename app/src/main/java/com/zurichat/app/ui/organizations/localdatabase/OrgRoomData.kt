package com.zurichat.app.ui.organizations.localdatabase

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.zurichat.app.models.organization_model.OrgData
import com.zurichat.app.ui.organizations.localdatabase.TypeConverters.OrgDataTypeConverter
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "OrganizationData")
data class OrgRoomData(
    @PrimaryKey
    val userID : String,
    @TypeConverters(OrgDataTypeConverter::class)
    val orgData: List<OrgData>
) : Parcelable
