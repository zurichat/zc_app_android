package com.zurichat.app.utils.views.list_item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.zurichat.app.R
import com.zurichat.app.data.model.Channel
import com.zurichat.app.databinding.ItemChannelBinding
import com.zurichat.app.ui.base.BaseItem
import com.zurichat.app.utils.show

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 23-Nov-21 at 4:12 PM
 *
 * Represents a channel item in a list
 * @param channel the channel to bind to this item
 */
open class ChannelItem(
    private val channel: Channel
) : BaseItem<Channel, ItemChannelBinding>(channel, R.layout.item_channel, channel.roomId){

    override fun inflate(parent: ViewGroup) =
        ItemChannelBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemChannelBinding) : Unit = with(binding){
        if(channel.isPrivate)
            // change the channel icon to private if the channel is private
            imageIChannelPrivate.setImageDrawable(
                ResourcesCompat.getDrawable(root.resources, R.drawable.ic_lock, null)
            )
        textIChannelName.text = channel.name
        // if the channel is unread, display the appropriate count, else leave the count bubble hidden
        iChannelUnread.root.apply{
            if(channel.unread > 0) {
                text = channel.unread.toString()
                show()
            }
        }
    }
}