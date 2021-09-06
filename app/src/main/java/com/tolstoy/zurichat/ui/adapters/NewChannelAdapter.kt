package com.tolstoy.zurichat.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.databinding.NewChannelItemBinding
import com.tolstoy.zurichat.models.NewChannel


class NewChannelAdapter(
    private val countries: List<NewChannel>,
) : RecyclerView.Adapter<NewChannelAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NewChannelItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int = countries.size

    inner class ViewHolder(private var item: NewChannelItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(chat: NewChannel) {
            item.channelItemPersonNameTxt.text = chat.name
            item.channelItemPersonIcon.setBackgroundResource(chat.iconResource)
            item.channelItemMessageTxt.text = chat.message

        }
    }
}
