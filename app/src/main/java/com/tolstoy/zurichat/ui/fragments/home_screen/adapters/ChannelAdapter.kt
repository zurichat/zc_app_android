package com.tolstoy.zurichat.ui.fragments.home_screen.adapters

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.models.Channel
import com.tolstoy.zurichat.models.ChannelModel

class ChannelAdapter(val context: Activity, private val list: List<ChannelModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClickListener: (() -> Unit)? = null
    private var onAddChannelClickListener: (() -> Unit)? = null

    fun setItemClickListener(listener: () -> Unit) {
        onItemClickListener = listener
    }

    fun setAddChannelClickListener(listener: () -> Unit) {
        onAddChannelClickListener = listener
    }

    inner class ChannelViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(channel: ChannelModel) {
            val fab = view.findViewById<FloatingActionButton>(R.id.fab)

            if (!channel.isPrivate){
                fab.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_hash))
            }else{
                fab.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_new_lock))
            }

            view.findViewById<TextView>(R.id.channelTitle).text = channel.name
            view.findViewById<ConstraintLayout>(R.id.root_layout).setOnClickListener {
                onItemClickListener?.invoke()
            }
        }
    }

    inner class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(channel: ChannelModel) {
            val nameView = view.findViewById<TextView>(R.id.headerTextView)
            nameView.text = channel.name

            /***
             * Checks if the header is a Unread Message Header Or Add New Channel Header.
             * Removes The Add Icon If it is a Unread Message Header
             * Sets A Click Listener If It is a Add Channel Header
             */
            if (channel.type == "channel_header_add"){
                view.findViewById<ConstraintLayout>(R.id.root_layout).setOnClickListener {
                    onAddChannelClickListener?.invoke()
                }
            }else{
                nameView.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0,0)
            }
        }
    }

    inner class DividerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(channel: Channel) {
            val nameView = view.findViewById<View>(R.id.divider)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        val view : View
        val viewHolder: RecyclerView.ViewHolder

        if (viewType == 0){
            view = context.layoutInflater.inflate(R.layout.channel_header_model, parent, false)
            viewHolder = HeaderViewHolder(view)
            return viewHolder
        }else if (viewType == 2){
            view = context.layoutInflater.inflate(R.layout.channel_divider_model, parent, false)
            viewHolder = DividerViewHolder(view)
            return viewHolder
        }
        view = context.layoutInflater.inflate(R.layout.channels_adapter, parent, false)
        viewHolder = ChannelViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChannelViewHolder) {
            holder.bind(list[position])
            //Perform Channel Related Actions Here
        }else if (holder is HeaderViewHolder){
            holder.bind(list[position])
            //Perform Header Related actions here
        }
    }

    override fun getItemCount() = list.size

    override fun getItemId(position: Int): Long {
        return list[position].hashCode().toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

}