package com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.util.viewBinding
import com.tolstoy.zurichat.databinding.FragmentChannelsBinding
import com.tolstoy.zurichat.ui.adapters.ReadChannelMessageAdapter
import com.tolstoy.zurichat.ui.adapters.UnreadChannelMessageAdapter


class ChannelsFragment : Fragment(R.layout.fragment_channels) {
    // TODO: Rename and change types of parameters

    private val binding by viewBinding(FragmentChannelsBinding::bind)
    //Dummy List for populating the recyclerView

    private var channelList = mutableListOf<Channel>(
        Channel("stage_4", true, false),
        Channel("announcement", false, false),
        Channel("comedy", false, true),
        Channel("team_tolstoy", true, true),
        Channel("resources", false, false),
        Channel("stage_5", true, false),
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        binding.unreadRecycler.layoutManager =
            LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        val adapt = UnreadChannelMessageAdapter(requireActivity(),channelList)
        binding.unreadRecycler.adapter = adapt

        binding.readRecycler.layoutManager =
            LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        val adapt2 = ReadChannelMessageAdapter(requireActivity(), channelList)
        binding.readRecycler.adapter = adapt2

    }

}
//Dummy Data for populating the recyclerView
data class Channel(var name: String, var privacy: Boolean, var read: Boolean )

