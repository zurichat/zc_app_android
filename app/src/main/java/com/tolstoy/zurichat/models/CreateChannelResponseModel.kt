package com.tolstoy.zurichat.models

data class CreateChannelResponseModel(
    val _id: String,
    val archived: String,
    val created_on: String,
    val description: String,
    val members: Int,
    val name: String,
    val owner: String,
    val `private`: String,
    val slug: String,
    val users: Users
)