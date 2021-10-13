package com.zurichat.app.models.network_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.zurichat.app.models.OrganizationMember

/*
* Jeffrey Orazulike [https://github.com/jeffreyorazulike]
* Created on 10/2/2021 at 10:20 PM 
*/

data class OrganizationData(
    val _id: String,
    val admins: Any,
    val created_at: String,
    val creator_email: String,
    val creator_id: String,
    val logo_url: String,
    val name: String,
    val plugins: Any,
    val settings: Any,
    val updated_at: String,
    val workspace_url: String
)

data class OrganizationCreatorResponse(
    val code: Int,
    val data: OrganizationCreatorIdData,
    val message: String
)

data class OrganizationCreatorIdData(
    val InsertedID: String
)

data class OrganizationCreator(
    val creator_email: String
)

data class Organization(
    val organizationData: OrganizationData,
    val message: String,
    val status: Int
)

data class UserOrganizationData(
    val _id: String,
    val imgs: ArrayList<String>,
    val isOwner: Boolean,
    val logo_url: String,
    val name: String,
    val no_of_members: Int,
    val workspace_url: String
)

data class OrganizationMembers(
    @Expose
    val status: Int = 0, // 200
    @Expose
    val message: String = "", // Members retrieved successfully
    @Expose
    val `data`: List<OrganizationMember> = listOf()
) {
    data class OrganizationMember(
        @SerializedName("_id")
        @Expose
        val id: String = "", // 616472111d7088ff14a42d9c
        @Expose
        val bio: String = "",
        @Expose
        val deleted: Boolean = false, // false
        @SerializedName("deleted_at")
        @Expose
        val deletedAt: String = "", // 0001-01-01T00:53:28+00:53
        @SerializedName("display_name")
        @Expose
        val displayName: String = "",
        @Expose
        val email: String = "", // zc_app_android@gmail.com
//        @Expose
//        val files: Any? = null, // null
        @SerializedName("first_name")
        @Expose
        val firstName: String = "",
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",
        @SerializedName("joined_at")
        @Expose
        val joinedAt: String = "", // 2021-10-11T19:19:13.603+02:00
        @Expose
        val language: String = "",
        @SerializedName("last_name")
        @Expose
        val lastName: String = "",
        @SerializedName("org_id")
        @Expose
        val orgId: String = "", // 616472111d7088ff14a42d9b
        @Expose
        val phone: String = "",
        @Expose
        val presence: String = "", // true
        @Expose
        val pronouns: String = "",
        @Expose
        val role: String = "", // owner
//        @Expose
//        val settings: Settings? = null,
//        @Expose
//        val socials: Any? = null, // null
//        @Expose
//        val status: Status = Status(),
        @SerializedName("time_zone")
        @Expose
        val timeZone: String = "",
        @SerializedName("user_name")
        @Expose
        val userName: String = "" // zc_app_android
    ) {
        fun name() = when {
            displayName.isNotBlank() -> displayName
            userName.isNotBlank() -> userName
            firstName.isNotBlank() -> "$firstName $lastName"
            else -> email
        }

        data class Settings(
            @Expose
            val accessibility: Accessibility = Accessibility(),
            @SerializedName("audio_and_video")
            @Expose
            val audioAndVideo: AudioAndVideo = AudioAndVideo(),
            @SerializedName("chat_settings")
            @Expose
            val chatSettings: ChatSettings = ChatSettings(),
            @SerializedName("languages_and_regions")
            @Expose
            val languagesAndRegions: LanguagesAndRegions = LanguagesAndRegions(),
            @SerializedName("mark_as_read")
            @Expose
            val markAsRead: MarkAsRead = MarkAsRead(),
            @SerializedName("messages_and_media")
            @Expose
            val messagesAndMedia: MessagesAndMedia = MessagesAndMedia(),
            @Expose
            val notifications: Notifications = Notifications(),
            @SerializedName("plugin_settings")
            @Expose
            val pluginSettings: Any? = null, // null
            @Expose
            val sidebar: Sidebar = Sidebar(),
            @Expose
            val themes: Themes = Themes()
        ) {
            data class Accessibility(
                @Expose
                val animation: Boolean = false, // false
                @SerializedName("direct_message_announcement")
                @Expose
                val directMessageAnnouncement: Any? = null, // null
                @SerializedName("press_empty_message_field")
                @Expose
                val pressEmptyMessageField: String = ""
            )

            data class AudioAndVideo(
                @SerializedName("enable_automatic_gain_control")
                @Expose
                val enableAutomaticGainControl: Boolean = false, // false
                @SerializedName("integrated_webcam")
                @Expose
                val integratedWebcam: String = "",
                @Expose
                val microphone: String = "",
                @Expose
                val speaker: String = "",
                @SerializedName("when_joining_a_huddle")
                @Expose
                val whenJoiningAHuddle: Any? = null, // null
                @SerializedName("when_joining_a_zuri_chat_call")
                @Expose
                val whenJoiningAZuriChatCall: Any? = null, // null
                @SerializedName("when_slack_is_in_the_background")
                @Expose
                val whenSlackIsInTheBackground: Any? = null // null
            )

            data class ChatSettings(
                @SerializedName("enter_is_send")
                @Expose
                val enterIsSend: Boolean = false, // false
                @SerializedName("font_size")
                @Expose
                val fontSize: String = "",
                @SerializedName("media_visibility")
                @Expose
                val mediaVisibility: Boolean = false, // false
                @Expose
                val theme: String = "",
                @Expose
                val wallpaper: String = ""
            )

            data class LanguagesAndRegions(
                @Expose
                val language: String = "",
                @SerializedName("languages_zuri_should_spell_check")
                @Expose
                val languagesZuriShouldSpellCheck: Any? = null, // null
                @SerializedName("set_time_zone_automatically")
                @Expose
                val setTimeZoneAutomatically: Boolean = false, // false
                @SerializedName("spell_check")
                @Expose
                val spellCheck: Boolean = false, // false
                @SerializedName("time_zone")
                @Expose
                val timeZone: String = ""
            )

            data class MarkAsRead(
                @Expose
                val language: Boolean = false, // false
                @SerializedName("when_i_view_a_channel")
                @Expose
                val whenIViewAChannel: String = ""
            )

            data class MessagesAndMedia(
                @SerializedName("additional_options")
                @Expose
                val additionalOptions: Any? = null, // null
                @SerializedName("bring_emails_into_zuri")
                @Expose
                val bringEmailsIntoZuri: String = "",
                @SerializedName("convert_emoticons_to_emoji")
                @Expose
                val convertEmoticonsToEmoji: Boolean = false, // false
                @Expose
                val custom: Boolean = false, // false
                @Expose
                val emoji: String = "",
                @SerializedName("emoji_as_text")
                @Expose
                val emojiAsText: Boolean = false, // false
                @SerializedName("frequently_used_emoji")
                @Expose
                val frequentlyUsedEmoji: Boolean = false, // false
                @SerializedName("inline_media_and_links")
                @Expose
                val inlineMediaAndLinks: Any? = null, // null
                @SerializedName("messages_one_click_reaction")
                @Expose
                val messagesOneClickReaction: Any? = null, // null
                @Expose
                val names: String = "",
                @SerializedName("show_jumbomoji")
                @Expose
                val showJumbomoji: Boolean = false, // false
                @Expose
                val theme: String = ""
            )

            data class Notifications(
                @SerializedName("channel_hurdle_notification")
                @Expose
                val channelHurdleNotification: Boolean = false, // false
                @SerializedName("deliver_notifications_via")
                @Expose
                val deliverNotificationsVia: String = "",
                @SerializedName("email_notifications_for_mentions")
                @Expose
                val emailNotificationsForMentions: Boolean = false, // false
                @SerializedName("flash_window_when_notification_comes")
                @Expose
                val flashWindowWhenNotificationComes: String = "",
                @SerializedName("meeting_replies_notification")
                @Expose
                val meetingRepliesNotification: Boolean = false, // false
                @SerializedName("message_preview_in_each_notification")
                @Expose
                val messagePreviewInEachNotification: Boolean = false, // false
                @SerializedName("mute_all_sounds")
                @Expose
                val muteAllSounds: Boolean = false, // false
                @SerializedName("my_keywords")
                @Expose
                val myKeywords: Any? = null, // null
                @SerializedName("notification_schedule")
                @Expose
                val notificationSchedule: String = "",
                @SerializedName("notify_me_about")
                @Expose
                val notifyMeAbout: String = "",
                @SerializedName("set_lounge_notifications_right")
                @Expose
                val setLoungeNotificationsRight: String = "",
                @SerializedName("set_message_notifications_right")
                @Expose
                val setMessageNotificationsRight: String = "",
                @SerializedName("thread_replies_notification")
                @Expose
                val threadRepliesNotification: Boolean = false, // false
                @SerializedName("use_different_settings_mobile")
                @Expose
                val useDifferentSettingsMobile: Boolean = false, // false
                @SerializedName("when_iam_not_active_on_desktop")
                @Expose
                val whenIamNotActiveOnDesktop: String = ""
            )

            data class Sidebar(
                @SerializedName("always_show_in_the_sidebar")
                @Expose
                val alwaysShowInTheSidebar: Any? = null, // null
                @SerializedName("list_private_channels_separately")
                @Expose
                val listPrivateChannelsSeparately: Boolean = false, // false
                @SerializedName("organize_external_conversations")
                @Expose
                val organizeExternalConversations: Boolean = false, // false
                @SerializedName("show_all_the_following")
                @Expose
                val showAllTheFollowing: String = "",
                @SerializedName("show_conversations")
                @Expose
                val showConversations: String = "",
                @SerializedName("show_profile_picture_next_to_dm")
                @Expose
                val showProfilePictureNextToDm: Boolean = false, // false
                @SerializedName("sidebar_sort")
                @Expose
                val sidebarSort: String = ""
            )

            data class Themes(
                @Expose
                val colors: String = "",
                @SerializedName("direct_messages_mentions_and_networks")
                @Expose
                val directMessagesMentionsAndNetworks: Boolean = false, // false
                @SerializedName("sync_with_os_setting")
                @Expose
                val syncWithOsSetting: Boolean = false, // false
                @Expose
                val themes: String = ""
            )
        }

        data class Status(
            @SerializedName("expiry_time")
            @Expose
            val expiryTime: String = "",
            @Expose
            val tag: String = "",
            @Expose
            val text: String = ""
        )
    }
}

data class UserOrganizationModel(val `data`: List<Data>, val message: String, val status: Int) {
    data class Data(
        val id: String,
        val imgs: List<String>,
        val isOwner: Boolean,
        val logo_url: String,
        val member_id: String,
        val name: String,
        val no_of_members: Int,
        val workspace_url: String
    )
}