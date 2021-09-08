package com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentChannelsBinding
import com.tolstoy.zurichat.models.Channel
import com.tolstoy.zurichat.ui.fragments.home_screen.adapters.ReadChannelMessageAdapter
import com.tolstoy.zurichat.ui.fragments.home_screen.adapters.UnreadChannelMessageAdapter


class ChannelsFragment : Fragment(R.layout.fragment_channels) {
    // TODO: Rename and change types of parameters


    private lateinit var binding: FragmentChannelsBinding
    //Dummy List for populating the recyclerView

    private var channelList = mutableListOf(
        Channel("stage_4", true, false),
        Channel("announcement", false, false),
        Channel("comedy", false, true),
        Channel("team_tolstoy", true, true),
        Channel("resources", false, false),
        Channel("stage_5", true, false),
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val adapt = UnreadChannelMessageAdapter(requireActivity(), channelList)

        adapt.setItemClickListener {
            findNavController().navigate(R.id.channelChatFragment)
        }
        binding.unreadRecycler.adapter = adapt


        val adapt2 = ReadChannelMessageAdapter(requireActivity(), channelList)

        adapt2.setItemClickListener {
            findNavController().navigate(R.id.channelChatFragment)
        }
        binding.readRecycler.adapter = adapt2

    }

}

