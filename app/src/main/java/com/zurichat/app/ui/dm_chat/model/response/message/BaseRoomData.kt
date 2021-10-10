package com.zurichat.app.ui.dm_chat.model.response.message

data class BaseRoomData(
    val getMessageResponse: Result?,
    val sendMessageResponse: SendMessageResponse?,
    val checkMessage: Boolean
){
    var id : String = ""
    init {
        id = if (checkMessage) {
            getMessageResponse!!.id
        } else {
            sendMessageResponse!!.message_id
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseRoomData

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}