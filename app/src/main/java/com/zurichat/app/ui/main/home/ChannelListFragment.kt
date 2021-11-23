package com.zurichat.app.ui.main.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.zurichat.app.R
import com.zurichat.app.data.model.Channel
import com.zurichat.app.databinding.FragmentChannelListBinding
import com.zurichat.app.ui.base.BaseFragment
import com.zurichat.app.utils.hide
import com.zurichat.app.utils.showSnackbar
import com.zurichat.app.utils.views.MarginItemDecoration
import com.zurichat.app.utils.views.viewBinding

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 25-Oct-21 at 9:07 PM
 *
 */
class ChannelListFragment: BaseFragment(R.layout.fragment_channel_list) {

    private val binding by viewBinding(FragmentChannelListBinding::bind)
    private val viewModel: HomeViewModel by lazy {
        (parentFragment as HomeFragment).viewModel
    }

    private val adapter by lazy {ChannelsAdapter()}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
    }

    private fun setupUI() = with(binding){
        cLViewMentions.apply {
            imageIChannelPrivate.hide()
            textIChannelName.text = getString(R.string.view_mentions)
            // remove the margin before the text
            textIChannelName.updateLayoutParams<ViewGroup.MarginLayoutParams> { marginStart = 0 }
            // TODO: Remove this code when the backend exposes an api for getting mentions
            iChannelUnread.root.hide()
        }
        // add spacing to the lists
        MarginItemDecoration(
            resources.getDimensionPixelSize(R.dimen.channel_item_margin),
            addSpacingToStart = false
        ).also { space ->
            listUnreadChannels.addItemDecoration(space)
            listJoinedChannels.addItemDecoration(space)
        }
    }

    private fun setupObservers() = with(viewModel){
        getJoinedChannels()
        channels.observe(viewLifecycleOwner){ updateUI(it) }
    }

    private fun updateUI(channelListState: HomeViewModel.ChannelListState) = with(binding){
        when(channelListState){
            is HomeViewModel.ChannelListState.Failure -> root.showSnackbar(R.string.retrieve_channels_error)
            is HomeViewModel.ChannelListState.Success -> {
                updateUnreadChannels(channelListState.channels.filter { channel -> channel.unread > 0 })
                updateJoinedChannels(channelListState.channels.filter { channel -> channel.unread == 0 })
            }
        }
    }

    private fun updateUnreadChannels(channels: List<Channel>) = with(binding.listUnreadChannels){
        adapter = ChannelsAdapter().apply{ submitList(channels) }
    }

    private fun updateJoinedChannels(channels: List<Channel>) = with(binding.listJoinedChannels){
        adapter = this@ChannelListFragment.adapter.apply{
            submitList(channels)
        }
    }
}