package com.zurichat.app.utils.views.list_item

import com.zurichat.app.data.model.Channel
import com.zurichat.app.databinding.ItemChannelBinding
import com.zurichat.app.utils.hide

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 07-Dec-21 at 6:50 AM
 *
 */
class ChannelBrowserItem(channel: Channel): ChannelItem(channel) {
    override fun bind(binding: ItemChannelBinding) {
        super.bind(binding)
        binding.iChannelUnread.root.hide()
    }
}