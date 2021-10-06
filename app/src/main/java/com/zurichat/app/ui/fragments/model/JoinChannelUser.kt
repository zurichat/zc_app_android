package com.zurichat.app.ui.fragments.model

data class JoinChannelUser(
    val _id : String
){
    val role_id : String = ""
    val is_admin : Boolean = false
    var starred : Boolean = false
    var notifications : Notifications? = null
}