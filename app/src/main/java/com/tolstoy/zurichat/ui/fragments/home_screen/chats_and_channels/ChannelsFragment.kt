package com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import androidx.recyclerview.widget.DiffUtil
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentChannelsBinding
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.fragments.home_screen.adapters.ChannelAdapter
import com.tolstoy.zurichat.ui.fragments.home_screen.diff_utils.ChannelDiffUtil
import com.tolstoy.zurichat.ui.fragments.networking.ChannelsList
import com.tolstoy.zurichat.ui.fragments.networking.RetrofitClientInstance
import com.tolstoy.zurichat.ui.fragments.viewmodel.ChannelViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class ChannelsFragment : Fragment(R.layout.fragment_channels) {
    private val viewModel : ChannelViewModel by viewModels()
    private lateinit var binding: FragmentChannelsBinding
    private lateinit var channelsArrayList: ArrayList<ChannelModel>
    private lateinit var originalChannelsArrayList: ArrayList<ChannelModel>
    private var user : User? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChannelsBinding.inflate(inflater, container, false)

        user = requireActivity().intent.extras?.getParcelable("USER")

        return binding.root
    }

    private lateinit var adapt:ChannelAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        channelsArrayList = ArrayList()
        originalChannelsArrayList = ArrayList()

        //addHeaders()
        getListOfChannels()
    }

    private fun generateRandomLong(): Long {
        val rand = Random(10000)
        return rand.nextLong()
    }

    /***
     * Headers Are Added Here. This will also be called after every update on the list to properly update the header positions
     */
    private fun addHeaders(){
        val newList: ArrayList<ChannelModel> = ArrayList()

        val unreadList: ArrayList<ChannelModel> = ArrayList()
        val unreadChannelHeader = ChannelModel(getString(R.string.unread_messages), false, false, "channel_header_unread", generateRandomLong().toString(), 0)

        val readList: ArrayList<ChannelModel> = ArrayList()
        val addChannelHeader = ChannelModel(getString(R.string.channels_), false, false, "channel_header_add", generateRandomLong().toString(), 0)
        val dividerHeader = ChannelModel("", false, false, "channel_header_add", generateRandomLong().toString(), 2)

        for (channel in channelsArrayList){
            if (channel.isRead){
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

            // Makes sure addition of divider is not repeated
            if (!newList.contains(dividerHeader)){
                newList.add(dividerHeader)
            }
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

        /**
         * Sets up adapter after channelList has been computed
         */
        adapt = ChannelAdapter(requireActivity(), channelsArrayList)
        adapt.setItemClickListener {
            findNavController().navigate(R.id.channelChatFragment)
        }
        adapt.setAddChannelClickListener {
            val bundle = Bundle()
            bundle.putParcelable("USER",user)
            bundle.putParcelableArrayList("Channels List",originalChannelsArrayList)
            findNavController().navigate(R.id.addChannelFragment,bundle)
        }
        binding.channelRecycleView.adapter = adapt
        diffResult.dispatchUpdatesTo(adapt)
    }

    /**
     * Getting The Channels List Is Ready Now.
     * Adding A Progressbar will be next
     */
    private fun getListOfChannels() {

        viewModel.getChannelsList()
        viewModel.channelsList.observe(viewLifecycleOwner,{
                channelsArrayList = it as ArrayList<ChannelModel>
                originalChannelsArrayList = it
                addHeaders()
        })
    }

}

