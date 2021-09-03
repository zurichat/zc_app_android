package com.tolstoy.zurichat.ui.adapters

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.models.DmMessages

class DmMessagesRecyclerAdapter(
    private val dmMessagesList: MutableList<DmMessages>,
    private val currentUser: String
) : RecyclerView.Adapter<DmMessagesRecyclerAdapter.DmMessagesRecyclerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DmMessagesRecyclerViewHolder {
        return DmMessagesRecyclerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.dm_chat_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DmMessagesRecyclerViewHolder, position: Int) {
        val dmMessages = dmMessagesList[position]
        holder.bind(dmMessages, currentUser)

        holder.dmChatLeftView.setOnCreateContextMenuListener { contextMenu, _, _ ->
            contextMenu.add("Copy").setOnMenuItemClickListener {
                val clipboard =
                    holder.itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", dmMessages.message)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(holder.itemView.context, "Copied to clipboard", Toast.LENGTH_SHORT)
                    .show()
                true
            }
        }
        holder.dmChatRightView.setOnCreateContextMenuListener { contextMenu, _, _ ->
            contextMenu.add("Copy").setOnMenuItemClickListener {
                val clipboard =
                    holder.itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", dmMessages.message)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(holder.itemView.context, "Copied to clipboard", Toast.LENGTH_SHORT)
                    .show()
                true
            }
        }
    }

    override fun getItemCount(): Int = dmMessagesList.size

    class DmMessagesRecyclerViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val dmChatLeftView: MaterialCardView = itemView.findViewById(R.id.dm_chat_left_view)
        private val dmChatLeftText: TextView = itemView.findViewById(R.id.dm_chat_left_text)
        private val dmChatLeftTextTime: TextView =
            itemView.findViewById(R.id.dm_chat_left_text_time)
        val dmChatRightView: MaterialCardView =
            itemView.findViewById(R.id.dm_chat_right_view)
        private val dmChatRightText: TextView = itemView.findViewById(R.id.dm_chat_right_text)
        private val dmChatRightTextTime: TextView =
            itemView.findViewById(R.id.dm_chat_right_text_time)

        fun bind(dmMessages: DmMessages, currentUser: String) {
            if (dmMessages.sender == currentUser) {
                dmChatLeftView.visibility = View.GONE
                dmChatRightView.visibility = View.VISIBLE
                dmChatRightText.text = dmMessages.message
                dmChatRightTextTime.text = dmMessages.time
            } else {
                dmChatRightView.visibility = View.GONE
                dmChatLeftView.visibility = View.VISIBLE
                dmChatLeftText.text = dmMessages.message
                dmChatLeftTextTime.text = dmMessages.time
            }
        }
    }
}