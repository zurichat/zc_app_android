package com.zurichat.app.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.zurichat.app.R
import com.zurichat.app.data.model.Channel
import com.zurichat.app.databinding.FragmentListBinding
import com.zurichat.app.ui.base.BaseFragment
import com.zurichat.app.ui.base.BaseItem
import com.zurichat.app.ui.base.BaseListAdapter
import com.zurichat.app.utils.show
import com.zurichat.app.utils.showSnackbar
import com.zurichat.app.utils.views.list_items.AlphabetItem
import com.zurichat.app.utils.views.list_items.ChannelItem
import com.zurichat.app.utils.views.viewBinding

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 29-Nov-21 at 6:17 PM
 *
 */
class ChannelBrowserFragment: BaseFragment(R.layout.fragment_list) {

    private val binding by viewBinding(FragmentListBinding::bind)
    private val viewModel by activityViewModels<HomeViewModel>()
    private val adapter = BaseListAdapter()
    private lateinit var originalList: List<Channel>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
    }

    private fun setupUI() = with(binding){
        lToolbar.root.also{ toolbar ->
            toolbar.show()
            toolbar.title = getString(R.string.channel_browser)
        }

        lList.adapter = this@ChannelBrowserFragment.adapter
    }

    private fun setupObservers() = with(viewModel){
        getJoinedChannels()
        channels.observe(viewLifecycleOwner){ updateUI(it) }
    }

    private fun updateUI(channelListState: HomeViewModel.ChannelListState) = with(binding){
        when(channelListState){
            is HomeViewModel.ChannelListState.Failure -> root.showSnackbar(R.string.retrieve_channels_error)
            is HomeViewModel.ChannelListState.Success -> {
                originalList = channelListState.channels
                updateChannelsList(channelListState.channels)
            }
        }
    }

    private fun updateChannelsList(channels: List<Channel>){
        if(channels.isEmpty()) return
        // get the alphabet for the first item and saves it in the list
        var currentAlphabet = channels[0].name[0]
        val alphabetizedList = mutableListOf<BaseItem<*, *>>().apply {
            add(AlphabetItem(currentAlphabet))
        }
        channels.sortedBy { it.name }.forEach { channel ->
            if(channel.name[0] != currentAlphabet){
                currentAlphabet = channel.name[0]
                alphabetizedList.add(AlphabetItem(currentAlphabet))
            }
            alphabetizedList.add(ChannelItem(channel))
        }
        adapter.submitList(alphabetizedList)
    }
}