package com.tolstoy.zurichat.ui.fragments.mentions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.tolstoy.zurichat.data.localSource.AppDatabase
import com.tolstoy.zurichat.databinding.FragmentMentionsBinding
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase.ChannelMessagesDao

class MentionsFragment : Fragment() {
    private lateinit var binding: FragmentMentionsBinding
    private lateinit var database: AppDatabase
    private lateinit var channelMessagesDao: ChannelMessagesDao

    private lateinit var user : User
    private lateinit var organizationID: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMentionsBinding.inflate(inflater, container, false)
        database = Room.databaseBuilder(requireActivity().applicationContext, AppDatabase::class.java, "zuri_chat").build()
        channelMessagesDao = database.channelMessagesDao()

        user = requireActivity().intent.extras?.getParcelable("USER")!!
        organizationID = "614679ee1a5607b13c00bcb7"
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.channelToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}