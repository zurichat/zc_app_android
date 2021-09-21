package com.tolstoy.zurichat.models

data class CreateChannelBodyModel(
    val description: String,
    val name: String,
    val owner: String,
    val `private`: Boolean
)