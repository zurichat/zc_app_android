package com.tolstoy.zurichat.ui.dm_chat.model.response.message

data class BaseRoomData(
    val getMessageResponse: Result?,
    val sendMessageResponse: SendMessageResponse?,
    val checkMessage: Boolean
)