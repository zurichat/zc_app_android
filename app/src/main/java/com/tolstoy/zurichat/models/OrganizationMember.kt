package com.tolstoy.zurichat.models


import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity(tableName = "members", primaryKeys = ["id", "orgId"])
data class OrganizationMember(
    @SerializedName("_id")
    @Expose
    val id: String = "", // 6145eee9285e4a18402074ce
    @SerializedName("bio")
    @Expose
    val bio: String = "",
    @SerializedName("deleted")
    @Expose
    val deleted: Boolean = false, // false
    @SerializedName("deleted_at")
    @Expose
    val deletedAt: String = LocalDateTime.now(ZoneOffset.UTC).toString(), // 0001-01-01T00:00:00Z
    @SerializedName("display_name")
    @Expose
    val displayName: String = "",
    @SerializedName("email")
    @Expose
    val email: String = "", // mukhtar.b017@gmail.com
    @SerializedName("files")
    @Expose
    val files: Files = Files(), // null
    @SerializedName("first_name")
    @Expose
    val firstName: String = "",
    @SerializedName("image_url")
    @Expose
    val imageUrl: String = "",
    @SerializedName("joined_at")
    @Expose
    val joinedAt: String = LocalDateTime.now(ZoneOffset.UTC).toString(), // 0001-01-01T00:00:00Z
    @SerializedName("last_name")
    @Expose
    val lastName: String = "",
    @SerializedName("org_id")
    @Expose
    val orgId: String = "", // 6145eee9285e4a18402074cd
    @SerializedName("phone")
    @Expose
    val phone: String = "",
    @SerializedName("presence")
    @Expose
    val presence: String = "", // true
    @SerializedName("pronouns")
    @Expose
    val pronouns: String = "",
    @SerializedName("role")
    @Expose
    val role: String = "", // owner
    @SerializedName("settings")
    @Expose
    val settings: Settings = Settings(), // null
    @SerializedName("socials")
    @Expose
    val socials: Socials = Socials(), // null
    @SerializedName("status")
    @Expose
    val status: String = "",
    @SerializedName("time_zone")
    @Expose
    val timeZone: String = "",
    @SerializedName("user_name")
    @Expose
    val userName: String = "" // mukhtar.b017
) {
    class Files {}
    class Settings {}
    class Socials {}
}