package com.tolstoy.zurichat.ui.add_channel

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentAddChannelBinding
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.ui.fragments.viewmodel.ChannelViewModel
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList


@AndroidEntryPoint
class AddChannelFragment : Fragment(R.layout.fragment_add_channel) {
    private val binding: FragmentAddChannelBinding by viewBinding(FragmentAddChannelBinding::bind)
    private lateinit var channelListAdapter: BaseListAdapter
    private val viewModel: ChannelViewModel by viewModels()
    private lateinit var channelsArrayList: ArrayList<ChannelModel>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        binding.channelToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupObservers() = with(binding) {
        viewModel.userItem.observe(viewLifecycleOwner) { user ->
            arguments?.let {
                channelsArrayList = it.getParcelableArrayList("Channels List")!!

                channelToolbar.subtitle =
                    channelsArrayList.size.toString().plus(" channels")

                val channelsWithAlphabetHeaders = createAlphabetizedChannelsList(channelsArrayList)

                channelListAdapter = BaseListAdapter { channelItem ->
                    val bundle1 = Bundle()
                    bundle1.putParcelable("USER", user)
                    bundle1.putParcelable("Channel", (channelItem as ListItem).channel)
                    findNavController().navigate(R.id.channelChatFragment, bundle1)
                }

                channelsList.adapter = channelListAdapter
                searchListEditText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {

                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {

                    }

                    override fun afterTextChanged(s: Editable?) {
                        val filteredChannelsWithAlphabetHeaders =
                            filterAlphabetizedChannelsList(channelsArrayList, s.toString().trim())
                        channelListAdapter.submitList(filteredChannelsWithAlphabetHeaders)
                    }

                })
                channelListAdapter.submitList(channelsWithAlphabetHeaders)
            }
        }
    }

    private fun createAlphabetizedChannelsList(channels: List<ChannelModel>): MutableList<BaseItem<*>> {
        // Wrap data in list items
        val channelsItems = channels.map {
            ListItem(it, requireActivity())
        }.sortedBy {
            it.channel.name.lowercase()
        }

        val channelsWithAlphabetHeaders = mutableListOf<BaseItem<*>>()

        // Loop through the channels list and add headers where we need them
        var currentHeader: String? = null
        channelsItems.forEach { c ->
            c.channel.name.firstOrNull()?.toString()?.let {
                if (it != currentHeader) {
                    channelsWithAlphabetHeaders.add(HeaderItem(it))
                    currentHeader = it
                }
            }
            channelsWithAlphabetHeaders.add(c)
        }
        return channelsWithAlphabetHeaders
    }

    private fun filterAlphabetizedChannelsList(
        channels: List<ChannelModel>,
        search: String
    ): MutableList<BaseItem<*>> {
        // Wrap data in list items
        val channelsItems = channels.map {
            ListItem(it, requireActivity())
        }.sortedBy {
            it.channel.name.lowercase()
        }

        val channelsWithAlphabetHeaders = mutableListOf<BaseItem<*>>()

        // Loop through the channels list and add headers where we need them
        var currentHeader: String? = null
        channelsItems.forEach { c ->
            if (c.channel.name.lowercase().contains(search.lowercase())) {
                c.channel.name.firstOrNull()?.toString()?.let {
                    if (it != currentHeader) {
                        channelsWithAlphabetHeaders.add(HeaderItem(it))
                        currentHeader = it
                    }
                }
                channelsWithAlphabetHeaders.add(c)
            }
        }
        return channelsWithAlphabetHeaders
    }
}