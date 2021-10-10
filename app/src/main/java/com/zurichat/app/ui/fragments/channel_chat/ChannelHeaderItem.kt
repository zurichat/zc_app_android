package com.zurichat.app.ui.fragments.channel_chat

import android.view.View
import com.zurichat.app.R
import com.zurichat.app.databinding.ChannelLayoutHeaderItemBinding
import com.zurichat.app.ui.add_channel.BaseItem

data class ChannelHeaderItem(val letter: String) : BaseItem<ChannelLayoutHeaderItemBinding> {

    override val layoutId = R.layout.channel_layout_header_item

    override val uniqueId = letter

    override fun initializeViewBinding(view: View) = ChannelLayoutHeaderItemBinding.bind(view)

    override fun bind(binding: ChannelLayoutHeaderItemBinding, itemClickCallback: ((BaseItem<ChannelLayoutHeaderItemBinding>) -> Unit)?) {
        binding.textHeader.text = letter
    }
}