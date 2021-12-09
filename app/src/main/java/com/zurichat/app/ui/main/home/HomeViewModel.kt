package com.zurichat.app.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.zurichat.app.data.model.Channel
import com.zurichat.app.data.model.Chat
import com.zurichat.app.ui.base.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 25-Oct-21 at 11:28 AM
 *
 */
class HomeViewModel: BaseViewModel(){
    private val _chats = MutableLiveData<ChatListState>()
    val chats: LiveData<ChatListState> get() = _chats

    private val _channels = MutableLiveData<ChannelListState>()
    val channels: LiveData<ChannelListState> get() = _channels

    private fun time() = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm"))

    fun getChats() = viewModelScope.launch {
        // TODO: Get data from real source
        _chats.value = ChatListState.EmptyChatList
        delay(6000)
        _chats.value = ChatListState.Success(listOf(
            Chat("", "jeffreyorazulike", "Have you implemented it",
                time(), 2, false, "jhjskdi"),
            Chat("", "hamidO.", "Meeting by 8:30pm",
                time(), 4, true, "jsduues"),
            Chat("", "cephas", "Your PR has been merged",
                time(), 0, true, "jhjsk5")
        ))
    }

    fun getJoinedChannels() = viewModelScope.launch {
        // TODO: Get data from real source
        delay(6000)
        _channels.value = ChannelListState.Success(listOf(
            Channel("Android-devs", 5, true, ""),
            Channel("Mentors", 9, true, ""),
            Channel("Design-stage-6", 3, true, ""),
            Channel("Chat-comedy-and-riddles", 0, false, ""),
            Channel("Random", 0, false, ""),
            Channel("Music", 0, false, ""),
            Channel("Chess", 0, false, ""),
        ))
    }

    suspend fun getMembers() = viewModelScope.async {
        delay(1000)
        return@async listOf("jeffreyorazulike","hamidO.","cephas", "PTech", "Vicksoson", "x5s")
    }.await()

    /**
     * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
     *
     * Represents the state of the chat list screen
     * */
    sealed class ChatListState {
        object EmptyChatList: ChatListState()
        class Success(val chats: List<Chat>): ChatListState()
    }

    /**
     * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
     *
     * Represents the state of the channel list screen
     * */
    sealed class ChannelListState {
        class Failure(val message: String): ChannelListState()
        class Success(val channels: List<Channel>): ChannelListState()
    }
}