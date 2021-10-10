package com.zurichat.app.ui.fragments.mentions

data class Mention(val channelID:String, val content: String, val senderID:String, val messageID:String, val timeStamp:String){
    var channelName : String? = null
}
