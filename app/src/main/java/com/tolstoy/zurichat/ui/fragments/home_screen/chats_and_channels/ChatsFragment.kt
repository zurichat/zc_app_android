package com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentChatsBinding
import com.tolstoy.zurichat.models.DmMessages
import com.tolstoy.zurichat.ui.fragments.home_screen.adapters.ChatsRVAdapter
import com.tolstoy.zurichat.ui.fragments.viewmodel.ChannelViewModel
import com.tolstoy.zurichat.ui.newchannel.NewChannelActivity
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class ChatsFragment : Fragment(R.layout.fragment_chats) {
    private val binding: FragmentChatsBinding by viewBinding(FragmentChatsBinding::bind)
    private val viewModel: ChannelViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val messages = listOf(
            DmMessages("Mary Basset", "Hey what's good", "22"),
            DmMessages("Druids", "Hey what's good", "1"),
            DmMessages("Kolade", "Hey what's good", "5"),
            DmMessages("Hamid.O", "Hey what's good", "3"),
            DmMessages("Luxanne", "Hey what's good", "7"),
            DmMessages("Cephas", "Hey what's good", "10"),
            DmMessages("Mark", "Hey what's good", "3"),
            DmMessages("John Victor", "Hey what's good", "2"),
            DmMessages("Hillary Jackson", "Hey what's good", "8"),
        )

        val chatsRVAdapter = ChatsRVAdapter(requireActivity(), messages)
        chatsRVAdapter.setItemClickListener {
            findNavController().navigate(R.id.dmFragment)

        }
        binding.recycler.adapter = chatsRVAdapter

        binding.fabAddChat.setOnClickListener {
            val intent = Intent(activity, NewChannelActivity::class.java)
            startActivity(intent)
        }

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.userItem.observe(viewLifecycleOwner) { user ->

        }
    }
}