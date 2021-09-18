package com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentChatsBinding
import com.tolstoy.zurichat.models.DmMessages
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.fragments.home_screen.adapters.ChatsRVAdapter

class ChatsFragment : Fragment() {
    private lateinit var binding: FragmentChatsBinding
    private var user : User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)

        user = requireActivity().intent.extras?.getParcelable("USER")

        return binding.root
    }

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
        binding.recycler.adapter = chatsRVAdapter

        binding.fabAddChat.setOnClickListener {
            findNavController().navigate(R.id.dmFragment)
        }
    }
}