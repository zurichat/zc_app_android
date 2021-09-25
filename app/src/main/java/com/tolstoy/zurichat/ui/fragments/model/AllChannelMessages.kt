package com.tolstoy.zurichat.ui.fragments.model

data class AllChannelMessages(
    var `data`: List<Data>,
    val message: String,
    val status: Int
)

class Notifications