package com.tolstoy.zurichat.models

//Data Class for populating the recyclerView
data class Channel(
    var name: String,
    var private: Boolean,
    var read: Boolean,
    var type: String,
    var _id: String,
    var viewType: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Channel

        if (_id != other._id) return false
        if (viewType != other.viewType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _id.hashCode()
        result = 31 * result + viewType
        return result
    }
}