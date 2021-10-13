package com.zurichat.app.data.localSource

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity(tableName = "organizationMembers")
@Parcelize
data class OrganizationMemberEntity(
    @PrimaryKey
    val id: String = "", // 6145eee9285e4a18402074ce
    val bio: String = "",
    val deleted: Boolean = false, // false
    val deletedAt: String = LocalDateTime.now(ZoneOffset.UTC).toString(), // 0001-01-01T00:00:00Z
    val displayName: String = "",
    val email: String = "", // mukhtar.b017@gmail.com
    val firstName: String = "",
    val imageUrl: String = "",
    val joinedAt: String = LocalDateTime.now(ZoneOffset.UTC).toString(), // 0001-01-01T00:00:00Z
    val lastName: String = "",
    val orgId: String = "", // 6145eee9285e4a18402074cd
    val phone: String = "",
    val presence: String = "", // true
    val pronouns: String = "",
    val role: String = "", // owner
    //val status: String = "",
    val timeZone: String = "",
    val userName: String = "" // mukhtar.b017
) : Parcelable
