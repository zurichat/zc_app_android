package com.tolstoy.zurichat.ui.chat.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentChatBinding
import com.tolstoy.zurichat.ui.chat.adapter.ChatsAdapter
import com.tolstoy.zurichat.ui.chat.model.Chat
import com.tolstoy.zurichat.util.viewBinding

class ChatsFragment : Fragment(R.layout.fragment_chat) {

    //binding layout using fragment view binding delegate
    private val binding by viewBinding(FragmentChatBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val chatAdapter = ChatsAdapter(retrieveData(),requireContext())

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = chatAdapter
        }
    }

    private fun retrieveData(): List<Chat> {
        return listOf(
            Chat(getString(R.string.chat_item_mary_value), getString(R.string.chat_item_dummy_message_hey), getString(R.string.chat_item_dummy_time), R.drawable.ic_mary_icon, 22,),
            Chat(getString(R.string.chat_item_druids), getString(R.string.chat_item_dummy_message_hey), getString(R.string.chat_item_dummy_time), R.drawable.ic_droids_icon, 22,),
            Chat(getString(R.string.chat_item_kolade_value), getString(R.string.chat_item_dummy_message_hey), getString(R.string.chat_item_dummy_time), R.drawable.ic_kolade_icon, 0,),
            Chat(getString(R.string.chat_item_luxanne_value), getString(R.string.chat_item_dummy_message_hey), getString(R.string.chat_item_dummy_time), R.drawable.ic_lux_icon, 0,),
            Chat(getString(R.string.chat_item_kolade_value), getString(R.string.chat_item_dummy_message_hey), getString(R.string.chat_item_dummy_time), R.drawable.ic_kolade_icon, 0,),
            Chat(getString(R.string.chat_item_kolade_value), getString(R.string.chat_item_dummy_message_hey), getString(R.string.chat_item_dummy_time), R.drawable.ic_kolade_icon, 0,),
            Chat(getString(R.string.chat_item_subomi_value), getString(R.string.chat_item_dummy_message_hey), getString(R.string.chat_item_dummy_time), R.drawable.ic_sub_icon, 0),
            Chat(getString(R.string.chat_item_kolade_value), getString(R.string.chat_item_dummy_message_hey), getString(R.string.chat_item_dummy_time), R.drawable.ic_kolade_icon, 0),
        )
    }

}
