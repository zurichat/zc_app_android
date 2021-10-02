package com.tolstoy.zurichat.ui.dm_chat.model.response.message

data class GetMessageResponse(
    val count: Int,
    val next: Any,
    val previous: Any,
    val results: List<Result>
)