package com.tolstoy.zurichat.ui.add_channel

import android.app.Activity
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ChannelsListItemBinding
import com.tolstoy.zurichat.models.ChannelModel

data class ListItem(val channel: ChannelModel, val context: Activity) : BaseItem<ChannelsListItemBinding> {

    override val layoutId = R.layout.channels_list_item
    override val uniqueId: String = channel._id

    override fun initializeViewBinding(view: View) = ChannelsListItemBinding.bind(view)

    override fun bind(binding: ChannelsListItemBinding, itemClickCallback: ((BaseItem<ChannelsListItemBinding>) -> Unit)?) {
        binding.root.setOnClickListener {
            itemClickCallback?.invoke(this)
        }
        binding.channelTitle.text = channel.name.lowercase()
        binding.badge.visibility = View.GONE
        binding.img.visibility = View.GONE

        if (!channel.isPrivate){
            binding.fab.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_hash))
        }else{
            binding.fab.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_new_lock))
        }

    }
}