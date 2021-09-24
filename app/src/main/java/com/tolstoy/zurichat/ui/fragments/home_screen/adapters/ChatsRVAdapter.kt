package com.tolstoy.zurichat.ui.fragments.home_screen.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ListItemBinding
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.models.DmMessages
import com.tolstoy.zurichat.ui.dm.DMFragment

class ChatsRVAdapter: RecyclerView.Adapter<ChatsRVAdapter.ChatViewHolder>() {

    private var onItemClickListener: ((dmMessages: DmMessages) -> Unit)? = null

    fun setItemClickListener(listener: (dmMessages:DmMessages) -> Unit) {
        onItemClickListener = listener
    }

    private val differCallback = object: DiffUtil.ItemCallback<DmMessages>(){
        override fun areItemsTheSame(oldItem: DmMessages, newItem: DmMessages): Boolean {
            return oldItem.sender == newItem.sender
        }

        override fun areContentsTheSame(oldItem: DmMessages, newItem: DmMessages): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)
    inner class ChatViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){
            fun bindItem(character: DmMessages){
                binding.textViewName.text = character.sender
                binding.textViewSpecie.text = character.message
                binding.textViewStatus.text = character.time

                binding.dmRoot.setOnClickListener {
//                    context.findNavController().navigate(R.id.direct_messages_screen)
                    onItemClickListener?.invoke(character)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            return ChatViewHolder(ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false))
        }

        override fun getItemCount(): Int {
            return differ.currentList.size
        }

        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            val cat = differ.currentList[position]
            return holder.bindItem(cat)
        }

}