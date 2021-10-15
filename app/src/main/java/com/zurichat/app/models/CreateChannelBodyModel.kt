package com.zurichat.app.models

data class CreateChannelBodyModel(
    val description: String,
    val name: String,
    val owner: String,
    val `private`: Boolean
)