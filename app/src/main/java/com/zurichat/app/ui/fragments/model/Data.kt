package com.zurichat.app.ui.fragments.model

data class Data(
    val _id: String?,
    val can_reply: Boolean?,
    val channel_id: String,
    val content: String?,
    val edited: Boolean?,
    val emojis: List<Emoji>?,
    val event: Event?,
    val files: List<String>?,
    val has_files: Boolean?,
    val pinned: Boolean?,
    val replies: Int?,
    val timestamp: String?,
    val type: String?,
    val user_id: String?,
    var profile_url: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Data

        if (_id != other._id) return false
        if (channel_id != other.channel_id) return false
        if (user_id != other.user_id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _id?.hashCode() ?: 0
        result = 31 * result + (channel_id?.hashCode() ?: 0)
        result = 31 * result + (user_id?.hashCode() ?: 0)
        return result
    }
}
