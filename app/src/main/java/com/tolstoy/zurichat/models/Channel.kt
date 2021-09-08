package com.tolstoy.zurichat.models

//Data Class for populating the recyclerView
/******
 * Name: Name Of Channel
 * Privacy: Public Or Private. True for private, false for public
 * Read: If Channel has unread messages or not
 * Type: To be used with the view holder so different views can be held.
 *       (Types Include channel, channel_header_add and channel_header_unread)
 *       Others Can Be Added later on as need be.
 * id: To be used with the Diff Util to identify each item. Might be changed later to a long
 * viewType: Only contains two values. 0 for the header, 1 for the channel body, 2 for divider
 */
data class Channel(
    var name: String,
    var privacy: Boolean,
    var read: Boolean,
    var type: String,
    var id: Long,
    var viewType: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Channel

        if (id != other.id) return false
        if (viewType != other.viewType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + viewType
        return result
    }
}