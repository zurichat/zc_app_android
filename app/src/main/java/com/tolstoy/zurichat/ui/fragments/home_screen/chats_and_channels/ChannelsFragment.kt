package com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentChannelsBinding
import com.tolstoy.zurichat.models.Channel
import com.tolstoy.zurichat.ui.fragments.home_screen.adapters.ChannelAdapter
import com.tolstoy.zurichat.ui.fragments.home_screen.diff_utils.ChannelDiffUtil
import java.util.ArrayList
import kotlin.random.Random


class ChannelsFragment : Fragment(R.layout.fragment_channels) {
    // TODO: Rename and change types of parameters

    private lateinit var binding: FragmentChannelsBinding
    //Dummy List for populating the recyclerView

    private var channelList = mutableListOf(
        Channel("stage-4", true, false,"channel",generateRandomLong(),1),
        Channel("announcement", true, false,"channel",generateRandomLong(),1),
        Channel("comedy", false, true,"channel",generateRandomLong(),1),
        Channel("team-tolstoy", false, true,"channel",generateRandomLong(),1),
        Channel("resources", true, false,"channel",generateRandomLong(),1),
        Channel("stage-5", true, false,"channel",generateRandomLong(),1),
        Channel("stage-6", true, false,"channel",generateRandomLong(),1),
        Channel("probation", true, false,"channel",generateRandomLong(),1),
        Channel("android", false, true,"channel",generateRandomLong(),1),
        Channel("general", false, true,"channel",generateRandomLong(),1),
        Channel("track-mobile", true, false,"channel",generateRandomLong(),1),
        Channel("random", true, false,"channel",generateRandomLong(),1)
    )
    private lateinit var channelsArrayList: ArrayList<Channel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var adapt:ChannelAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        channelsArrayList = ArrayList()
        channelsArrayList.addAll(channelList)

        adapt = ChannelAdapter(requireActivity(), channelsArrayList)
        adapt.setItemClickListener {
            findNavController().navigate(R.id.channelChatFragment)
        }
        binding.channelRecycleView.adapter = adapt
        addHeaders()
    }

    private fun generateRandomLong(): Long {
        val rand = Random(10000)
        return rand.nextLong()
    }

    /***
     * Headers Are Added Here. This will also be called after every update on the list to properly update the header positions
     */
    private fun addHeaders(){
        val newList: ArrayList<Channel> = ArrayList()

        val unreadList: ArrayList<Channel> = ArrayList()
        val unreadChannelHeader = Channel(
            name = getString(R.string.unread_messages),
            privacy = false,
            read = false,
            type = "channel_header_unread",
            id = generateRandomLong(),
            viewType = 0
        )

        val readList: ArrayList<Channel> = ArrayList()
        val addChannelHeader = Channel(
            name = getString(R.string.channels_),
            privacy = false,
            read = false,
            type = "channel_header_add",
            id = generateRandomLong(),
            viewType = 0
        )

        val dividerHeader = Channel(
            name = "",
            privacy = false,
            read = false,
            type = "channel_header_add",
            id = generateRandomLong(),
            viewType = 2
        )

        for (channel in channelsArrayList){
            if (channel.read){
                readList.add(channel)
            }else{
                unreadList.add(channel)
            }
        }
        if (unreadList.size>0){
            newList.add(unreadChannelHeader)
            for (channel in unreadList){
                //This makes sure there are no duplicate headers
                if (!(channel.name == getString(R.string.unread_messages) || channel.name == getString(R.string.channels_))){
                    newList.add(channel)
                }
            }
            newList.add(dividerHeader)
        }
        newList.add(addChannelHeader)
        for (channel in readList){
            //This makes sure there are no duplicate headers
            if (!(channel.name == getString(R.string.unread_messages) || channel.name == getString(R.string.channels_))){
                newList.add(channel)
            }
        }

        val diffResult = DiffUtil.calculateDiff(ChannelDiffUtil(channelsArrayList, newList))
        channelsArrayList.clear()
        channelsArrayList.addAll(newList)
        diffResult.dispatchUpdatesTo(adapt)
    }

}

