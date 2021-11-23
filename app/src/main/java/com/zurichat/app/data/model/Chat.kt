package com.zurichat.app.data.model

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 29-Oct-21 at 7:51 AM
 *
 * Represents a chat in the home screen
 *
 * @property image the image of the other user that is participating in this conversation
 * @property name the name of the other user that is participating in this conversation
 * @property message the text of the last sent message
 * @property time the time the last message was sent
 * @property unread the amount of messages that haven't ben read by this user in the conversation
 * @property online is the other user online
 * @property roomId the id of the room this chat represents
 */
data class Chat(
    val image: String,
    val name: String,
    val message: String,
    val time: String,
    val unread: Int,
    val online: Boolean,
    val roomId: String
)