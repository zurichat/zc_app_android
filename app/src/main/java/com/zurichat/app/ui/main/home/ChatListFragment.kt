package com.zurichat.app.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentChatListBinding
import com.zurichat.app.ui.base.BaseFragment
import com.zurichat.app.ui.base.BaseListAdapter
import com.zurichat.app.utils.views.list_item.ChatItem
import com.zurichat.app.utils.views.list_item.MemberItem
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

    private val adapter = BaseListAdapter()

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
                listAvailableTeammates.adapter = BaseListAdapter().also{
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
    companion object {
        val TAG = ChatListFragment::class.simpleName
    }
}