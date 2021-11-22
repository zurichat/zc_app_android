package com.zurichat.app.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.R
import com.zurichat.app.databinding.ItemChannelBinding
import com.zurichat.app.ui.main.home.domain.Channel
import com.zurichat.app.utils.show

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 13-Nov-21 at 12:58 PM
 *
 */
class ChannelsAdapter : ListAdapter<Channel, ChannelsAdapter.ViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChannelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ViewHolder(private val binding: ItemChannelBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(channel: Channel) = with(binding){
            if(channel.isPrivate)
                imageIChannelPrivate.setImageDrawable(
                    ResourcesCompat.getDrawable(root.resources, R.drawable.ic_lock, null))
            textIChannelName.text = channel.name
            iChannelUnread.root.apply{
                if(channel.unread > 0) {
                    text = channel.unread.toString()
                    show()
                }
            }
        }
    }

    companion object {
        val TAG = ChannelsAdapter::class.simpleName
        private val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Channel>() {
            override fun areItemsTheSame(oldItem: Channel, newItem: Channel): Boolean {
                return oldItem.roomId == newItem.roomId
            }
            override fun areContentsTheSame(oldItem: Channel, newItem: Channel): Boolean {
                return oldItem == newItem
            }
        }
    }
}