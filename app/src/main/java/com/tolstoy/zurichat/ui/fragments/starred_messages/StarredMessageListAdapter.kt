package com.tolstoy.zurichat.ui.fragments.starred_messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.models.StarredMessages

class StarredMessageListAdapter :
    ListAdapter<StarredMessages, StarredMessageListAdapter.StarredMessageViewHolder>(
        StarredMessagesComparator()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarredMessageViewHolder {
        return StarredMessageViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: StarredMessageViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class StarredMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textMessage: TextView = itemView.findViewById(R.id.text_message)

        fun bind(starredMessages: StarredMessages) {
            textMessage.text = starredMessages.message
        }

        companion object {
            fun create(parent: ViewGroup): StarredMessageViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_starred_messages, parent, false)
                return StarredMessageViewHolder(view)
            }
        }
    }

    class StarredMessagesComparator : DiffUtil.ItemCallback<StarredMessages>() {
        override fun areItemsTheSame(oldItem: StarredMessages, newItem: StarredMessages): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: StarredMessages,
            newItem: StarredMessages
        ): Boolean {
            return oldItem.message == newItem.message
        }
    }
}