package com.zurichat.app.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.ZoneOffset

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
    val files: Any? = null, // null
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
    val settings: Any? = null, // null
    @SerializedName("socials")
    @Expose
    val socials: Any? = null, // null
    @SerializedName("status")
    @Expose
    val status: Any? = null,
    @SerializedName("time_zone")
    @Expose
    val timeZone: String = "",
    @SerializedName("user_name")
    @Expose
    val userName: String = "" // mukhtar.b017
){
    fun name() = when {
        displayName.isNotBlank() -> displayName
        userName.isNotBlank() -> userName
        firstName.isNotBlank() -> "$firstName $lastName"
        else -> email.substringBefore('@')
    }
}