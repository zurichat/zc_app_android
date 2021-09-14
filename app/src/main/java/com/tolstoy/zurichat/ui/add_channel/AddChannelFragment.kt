package com.tolstoy.zurichat.ui.add_channel

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentAddChannelBinding
import com.tolstoy.zurichat.databinding.FragmentChannelChatBinding
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.models.User
import java.util.ArrayList

class AddChannelFragment : Fragment() {
    private lateinit var binding: FragmentAddChannelBinding
    private lateinit var channelListAdapter : BaseListAdapter

    private var user : User? = null
    private lateinit var channelsArrayList: ArrayList<ChannelModel>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddChannelBinding.inflate(inflater, container, false)
        val bundle = arguments
        if (bundle != null) {
            user = bundle.getParcelable("USER")
            channelsArrayList = bundle.getParcelableArrayList("Channels List")!!

            binding.channelToolbar.subtitle = channelsArrayList.size.toString().plus(" channels")

            val channelsWithAlphabetHeaders = createAlphabetizedChannelsList(channelsArrayList)

            channelListAdapter = BaseListAdapter {

            }

            binding.channelsList.adapter = channelListAdapter
            binding.searchListEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    val filteredChannelsWithAlphabetHeaders = filterAlphabetizedChannelsList(channelsArrayList,s.toString().trim())
                    channelListAdapter.submitList(filteredChannelsWithAlphabetHeaders)
                }

            })
            channelListAdapter.submitList(channelsWithAlphabetHeaders)
        }

        return binding.root
    }

    private fun createAlphabetizedChannelsList(channels: List<ChannelModel>): MutableList<BaseItem<*>> {
        // Wrap data in list items
        val channelsItems = channels.map {
            ListItem(it,requireActivity())
        }.sortedBy {
            it.channel.name.lowercase()
        }

        val channelsWithAlphabetHeaders = mutableListOf<BaseItem<*>>()

        // Loop through the channels list and add headers where we need them
        var currentHeader: String? = null
        channelsItems.forEach{ c->
            c.channel.name.firstOrNull()?.toString()?.let {
                if (it != currentHeader){
                    channelsWithAlphabetHeaders.add(HeaderItem(it))
                    currentHeader = it
                }
            }
            channelsWithAlphabetHeaders.add(c)
        }
        return channelsWithAlphabetHeaders
    }

    private fun filterAlphabetizedChannelsList(channels: List<ChannelModel>, search: String): MutableList<BaseItem<*>> {
        // Wrap data in list items
        val channelsItems = channels.map {
            ListItem(it,requireActivity())
        }.sortedBy {
            it.channel.name.lowercase()
        }

        val channelsWithAlphabetHeaders = mutableListOf<BaseItem<*>>()

        // Loop through the channels list and add headers where we need them
        var currentHeader: String? = null
        channelsItems.forEach{ c->
            if(c.channel.name.lowercase().contains(search.lowercase())){
                c.channel.name.firstOrNull()?.toString()?.let {
                    if (it != currentHeader){
                        channelsWithAlphabetHeaders.add(HeaderItem(it))
                        currentHeader = it
                    }
                }
                channelsWithAlphabetHeaders.add(c)
            }
        }
        return channelsWithAlphabetHeaders
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}