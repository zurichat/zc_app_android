package com.zurichat.app.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.zurichat.app.R
import com.zurichat.app.data.model.Channel
import com.zurichat.app.databinding.FragmentListBinding
import com.zurichat.app.ui.base.BaseFragment
import com.zurichat.app.ui.base.BaseItem
import com.zurichat.app.ui.base.BaseListAdapter
import com.zurichat.app.utils.showSnackbar
import com.zurichat.app.utils.views.MarginItemDecoration
import com.zurichat.app.utils.views.list_items.ChannelItem
import com.zurichat.app.utils.views.list_items.DividerItem
import com.zurichat.app.utils.views.list_items.TitleItem
import com.zurichat.app.utils.views.list_items.ViewMentionItem
import com.zurichat.app.utils.views.viewBinding

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 25-Oct-21 at 9:07 PM
 *
 */
class ChannelListFragment: BaseFragment(R.layout.fragment_list) {

    private val binding by viewBinding(FragmentListBinding::bind)
    private val viewModel: HomeViewModel by lazy {
        (parentFragment as HomeFragment).viewModel
    }

    private val adapter by lazy { BaseListAdapter() }
    private val listSpacing by lazy { MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.channel_item_margin)) }
    private val defaultItems by lazy {
        listOf(ViewMentionItem(), TitleItem(getString(R.string.unread_messages)))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupObservers()
    }

    private fun setupUI() = binding.lList.also{ list ->
        // add the view mentions item and unread messages header to the list
        adapter.submitList(defaultItems)
        // add spacing to the lists
        list.addItemDecoration(listSpacing)
        // register the adapter to the list
        list.adapter = adapter
    }

    private fun setupObservers() = with(viewModel){
        getJoinedChannels()
        channels.observe(viewLifecycleOwner){ updateUI(it) }
    }

    private fun updateUI(channelListState: HomeViewModel.ChannelListState) = with(binding){
        when(channelListState){
            is HomeViewModel.ChannelListState.Failure -> root.showSnackbar(R.string.retrieve_channels_error)
            is HomeViewModel.ChannelListState.Success -> updateChannelsList(channelListState.channels)
        }
    }

    private fun updateChannelsList(channels: List<Channel>){
        val updatedItemList = mutableListOf<BaseItem<*,*>>().apply {
            // add the default items to the list
            addAll(defaultItems)
            // add the unread channels to the list
            addAll(channels.asSequence()
                .filter { it.unread > 0 }.map { ChannelItem(it) }.sortedBy { it.item.name }.toList())
            // add the divider to the list
            DividerItem().apply {
                add(this)
                listSpacing.exclude(uniqueId as Int)
            }
            // add the add channel item to the list
            add(TitleItem(getString(R.string.channels), true){
                findNavController().navigate(R.id.action_homeFragment_to_channelBrowserFragment)
            })
            // add the remaining channels to the list
            addAll(channels.asSequence()
                .filter { it.unread == 0 }.map { ChannelItem(it) }.sortedBy { it.item.name }.toList())
        }
        adapter.submitList(updatedItemList)
    }
}