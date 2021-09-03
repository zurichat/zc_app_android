package com.tolstoy.zurichat.ui.adapters

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.fragment.Channel
import com.tolstoy.zurichat.databinding.ChannelsAdapterBinding

class ReadChannelMessageAdapter(val context: Activity, private val list: List<Channel>):
    RecyclerView.Adapter<ReadChannelMessageAdapter.CustomViewHolder>() {



    inner class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
       init {

       }
        fun bind(channel: Channel) {
         view.findViewById<TextView>(R.id.channelTitle).text = channel.name
        }
   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
     val view = context.layoutInflater.inflate(R.layout.channels_adapter, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}