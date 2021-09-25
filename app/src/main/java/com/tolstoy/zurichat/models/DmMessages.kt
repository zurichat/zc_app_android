package com.tolstoy.zurichat.models

import com.tolstoy.zurichat.data.remoteSource.DmService

data class DmMessages(
    val sender: String,
    val message: String,
    val time: String)
