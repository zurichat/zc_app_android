package com.zurichat.app.ui.dm_chat.model.response.member

data class Notifications(
    val channel_hurdle_notification: Boolean,
    val email_notifications_for_mentions_and_dm: Any,
    val message_preview_in_each_notification: Boolean,
    val mute_all_sounds: Boolean,
    val my_keywords: String,
    val notification_schedule: String,
    val notify_me_about: String,
    val thread_replies_notification: Boolean,
    val use_different_settings_mobile: String,
    val when_iam_not_active_on_desktop: String
)