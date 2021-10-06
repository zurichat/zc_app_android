package com.zurichat.app.ui.add_channel

import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import com.zurichat.app.R
import com.zurichat.app.databinding.ChannelsListItemBinding
import com.zurichat.app.models.ChannelModel
import java.util.ArrayList

data class ListItem(val channel: ChannelModel, val context: Activity, val joinedChannelsArrayList: ArrayList<ChannelModel>) : BaseItem<ChannelsListItemBinding> {

    override val layoutId = R.layout.channels_list_item
    override val uniqueId: String = channel._id

    override fun initializeViewBinding(view: View) = ChannelsListItemBinding.bind(view)

    override fun bind(binding: ChannelsListItemBinding, itemClickCallback: ((BaseItem<ChannelsListItemBinding>) -> Unit)?) {
        binding.root.setOnClickListener {
            itemClickCallback?.invoke(this)
        }

        if (joinedChannelsArrayList.contains(channel)){
            binding.joinedButton.visibility = View.VISIBLE
        }else{
            binding.joinedButton.visibility = View.GONE
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