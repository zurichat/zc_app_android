package com.zurichat.app.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.zurichat.app.R
import com.zurichat.app.data.model.Chat
import com.zurichat.app.databinding.FragmentChatListBinding
import com.zurichat.app.databinding.ItemAttachmentImageBinding
import com.zurichat.app.databinding.ItemChatBinding
import com.zurichat.app.ui.base.BaseFragment
import com.zurichat.app.ui.base.BaseItem
import com.zurichat.app.ui.base.BaseListAdapter
import com.zurichat.app.utils.show
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

    private val adapter = BaseListAdapter(null)

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
                adapter.submitList(chatState.chats.map { ChatItem(it) })
            }
            HomeViewModel.ChatListState.EmptyChatList -> lifecycleScope.launch {
                val members = viewModel.getMembers()
                // show the user that they haven't sent any messages yet
                groupChatBlank.visibility = View.VISIBLE
                val count = 3
                // show some members of the organization
                listAvailableTeammates.adapter = BaseListAdapter(null).also{
                    it.submitList(members.subList(0, count).map{ MemberItem(it) })
                }
                textAvailableTeammates.text = getString(
                    R.string.mates_on_zuri_chat,
                    // show the names of the first $count members
                    members.subList(0, count).reduceOrNull { acc, s -> "$acc $s" },
                    members.size - count
                )
            }
        }
    }

    class ChatItem(
        private val chat: Chat
    ) : BaseItem<Chat, ItemChatBinding>(chat, R.layout.item_chat, chat.roomId) {

        override fun initializeViewBinding(view: View) = ItemChatBinding.bind(view)

        override fun bind(
            binding: ItemChatBinding,
            itemClickCallback: ((BaseItem<Chat, ItemChatBinding>) -> Unit)?
        ): Unit = with(binding) {
            Glide.with(root.context).load(chat.image)
                .placeholder(R.drawable.ic_person).into(imageChatUser)
            imageChatOnline.isVisible = chat.online
            textChatUsername.text = chat.name
            textChatLastMessage.text = chat.message
            textChatTime.text = chat.time
            chatUnreadMessages.root.apply {
                if(chat.unread > 0) show()
                text = chat.unread.toString()
            }
        }
    }

    class MemberItem(
        private val member: String
    ) : BaseItem<String, ItemAttachmentImageBinding>(
        member, R.layout.item_attachment_image, member
    ){
        override fun initializeViewBinding(view: View) = ItemAttachmentImageBinding.bind(view)

        override fun bind(
            binding: ItemAttachmentImageBinding,
            itemClickCallback: ((BaseItem<String, ItemAttachmentImageBinding>) -> Unit)?
        ) : Unit = with(binding){
            root.updateLayoutParams {
                width = 50.toDp(root.resources)
            }
            Glide.with(root.context).load(member)
                .placeholder(R.drawable.ic_person)
                .into(imageIAI)
        }
    }

    companion object {
        val TAG = ChatListFragment::class.simpleName
    }
}