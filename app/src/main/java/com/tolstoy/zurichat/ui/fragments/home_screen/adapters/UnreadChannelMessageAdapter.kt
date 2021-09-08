package com.tolstoy.zurichat.ui.fragments.home_screen.adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.models.Channel

class UnreadChannelMessageAdapter(val context: Activity, private val list: List<Channel>) :
    RecyclerView.Adapter<UnreadChannelMessageAdapter.CustomViewHolder>() {


    private var onItemClickListener: (() -> Unit)? = null

    fun setItemClickListener(listener: () -> Unit) {
        onItemClickListener = listener
    }


    inner class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(channel: Channel) {
            view.findViewById<TextView>(R.id.channelTitle).text = channel.name
            view.findViewById<ConstraintLayout>(R.id.root_layout).setOnClickListener {
                onItemClickListener?.invoke()
            }
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