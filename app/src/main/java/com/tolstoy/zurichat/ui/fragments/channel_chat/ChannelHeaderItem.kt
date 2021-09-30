package com.tolstoy.zurichat.ui.fragments.channel_chat

import android.view.View
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ChannelLayoutHeaderItemBinding
import com.tolstoy.zurichat.databinding.LayoutHeaderItemBinding
import com.tolstoy.zurichat.ui.add_channel.BaseItem

data class ChannelHeaderItem(val letter: String) : BaseItem<ChannelLayoutHeaderItemBinding> {

    override val layoutId = R.layout.channel_layout_header_item

    override val uniqueId = letter

    override fun initializeViewBinding(view: View) = ChannelLayoutHeaderItemBinding.bind(view)

    override fun bind(binding: ChannelLayoutHeaderItemBinding, itemClickCallback: ((BaseItem<ChannelLayoutHeaderItemBinding>) -> Unit)?) {
        binding.textHeader.text = letter
    }
}