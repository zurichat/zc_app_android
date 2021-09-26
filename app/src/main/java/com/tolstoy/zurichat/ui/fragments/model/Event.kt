package com.tolstoy.zurichat.ui.fragments.model

data class Event(
    val action: String,
    val recipients: List<Recipient>
)