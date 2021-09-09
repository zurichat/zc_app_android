package com.tolstoy.zurichat.models

import java.time.LocalTime

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created 01-Sep-21 at 6:03 PM
 *
 * @param userId is the id of the user that sent the message
 * @param content is the message content
 * @param time is the time the message was sent
 */
data class Message(
    /**The id of the user that sent the message*/
    val userId: Int,
    /**The content of the message*/
    val content: String,
    /**The time the message was sent*/
    val time: LocalTime = LocalTime.now())
