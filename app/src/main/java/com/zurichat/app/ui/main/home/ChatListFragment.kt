package com.zurichat.app.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentChatListBinding
import com.zurichat.app.databinding.ItemAttachmentImageBinding
import com.zurichat.app.databinding.ItemChatBinding
import com.zurichat.app.ui.base.BaseFragment
import com.zurichat.app.ui.main.home.domain.Chat
import com.zurichat.app.utils.toDp
import com.zurichat.app.utils.views.viewBinding
import kotlinx.coroutines.launch

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 25-Oct-21 at 9:06 PM
 *
 */
class ChatListFragment: BaseFragment(R.layout.fragment_chat_list) {

    private val binding by viewBinding(FragmentChatListBinding::bind)
    private val viewModel: HomeViewModel by lazy {
        (parentFragment as HomeFragment).viewModel
    }

    private val adapter = ChatsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
    }

    private fun setupUI(): Unit = with(binding){
        listChats.adapter = this@ChatListFragment.adapter
    }

    private fun setupObservers(): Unit = with(viewModel){
        getChats()
        chats.observe(viewLifecycleOwner){ displayChats(it) }
    }

    private fun displayChats(chatState: HomeViewModel.ChatListState) = with(binding){
        when(chatState){
            is HomeViewModel.ChatListState.Success -> {
                groupChatBlank.visibility = View.GONE
                // populate the chat list
                adapter.submitList(chatState.chats)
            }
            HomeViewModel.ChatListState.EmptyChatList -> lifecycleScope.launch {
                val members = viewModel.getMembers()
                // show the user that they haven't sent any messages yet
                groupChatBlank.visibility = View.VISIBLE
                val count = 3
                // show some members of the organization
                listAvailableTeammates.adapter = ChatMembersAdapter(members.subList(0, count))
                textAvailableTeammates.text = getString(
                    R.string.mates_on_zuri_chat,
                    // show the names of the first $count members
                    members.subList(0, count).reduceOrNull { acc, s -> "$acc $s" },
                    members.size - count
                )
            }
        }
    }

    class ChatsAdapter: ListAdapter<Chat, ChatsAdapter.ChatViewHolder>(DIFF_CALLBACK) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            return ChatViewHolder(ItemChatBinding
                .inflate(LayoutInflater.from(parent.context), parent, false))
        }

        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            holder.bind(currentList[position])
        }

        class ChatViewHolder(private val binding: ItemChatBinding):
            RecyclerView.ViewHolder(binding.root){

            fun bind(chat: Chat): Unit = with(binding){
                Glide.with(root.context).load(chat.image)
                    .placeholder(R.drawable.ic_person).into(imageChatUser)
                imageChatOnline.isVisible = chat.online
                textChatUsername.text = chat.name
                textChatLastMessage.text = chat.message
                textChatTime.text = chat.time
                textChatUnreadMessages.apply {
                    isVisible = chat.unread > 0
                    text = chat.unread.toString()
                }
            }
        }
    }

    class ChatMembersAdapter(private val images: List<String>):
        RecyclerView.Adapter<ChatMembersAdapter.ChatMembersViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMembersViewHolder {
            return ChatMembersViewHolder(
                ItemAttachmentImageBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }

        override fun onBindViewHolder(holder: ChatMembersViewHolder, position: Int) {
            holder.bind(images[position])
        }

        override fun getItemCount() = images.size

        class ChatMembersViewHolder(private val binding: ItemAttachmentImageBinding):
            RecyclerView.ViewHolder(binding.root){

            fun bind(image: String): Unit = with(binding){
                root.updateLayoutParams {
                    width = 50.toDp(root.resources)
                }
                Glide.with(root.context).load(image)
                    .placeholder(R.drawable.ic_person)
                    .into(imageIAI)
            }
        }
    }

    companion object {
        val TAG = ChatListFragment::class.simpleName
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem.roomId == newItem.roomId
            }
            override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem == newItem
            }
        }
    }
}