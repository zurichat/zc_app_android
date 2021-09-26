package com.tolstoy.zurichat.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.NewChannelItemBinding
import com.tolstoy.zurichat.models.NewChannel
import com.tolstoy.zurichat.models.User


class NewChannelAdapter: RecyclerView.Adapter<NewChannelAdapter.ViewHolder>() {
    var list = emptyList<User>()
    var listener: ((Int) -> Unit)? = null

    fun setItemClickListener(listener: (Int) -> Unit){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            NewChannelItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(position)

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(private var item: NewChannelItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(position: Int) {
            val chat = list[position]
            item.channelItemPersonNameTxt.text =
                if(chat.first_name.isEmpty() && chat.last_name.isEmpty()) "No name"
                else "${chat.first_name} ${chat.last_name}"


            item.channelItemPersonIcon.setBackgroundResource(R.drawable.ic_kolade_icon)
            item.channelItemMessageTxt.text = chat.email
            item.root.setOnClickListener {
                listener?.invoke(position)
            }
        }
    }
}
