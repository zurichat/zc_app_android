package com.zurichat.app.data.model

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 12-Nov-21 at 1:41 AM
 *
 * Represents a channel in the home screen
 *
 * @property name the name of this channel
 * @property unread the amount of messages that haven't ben read by this user in this channel
 * @property isPrivate is this channel only joined by invitation
 * @property roomId the id of the room this channel represents
 */
data class Channel(
    val name: String,
    val unread: Int,
    val isPrivate: Boolean,
    val roomId: String
)
