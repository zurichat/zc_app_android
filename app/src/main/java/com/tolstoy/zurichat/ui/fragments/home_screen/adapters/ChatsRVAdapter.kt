package com.tolstoy.zurichat.ui.fragments.home_screen.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ListItemBinding
import com.tolstoy.zurichat.models.DmMessages
import com.tolstoy.zurichat.ui.dm.DMFragment

class ChatsRVAdapter(val context: Activity, private val characters: List<DmMessages>): RecyclerView.Adapter<ChatsRVAdapter.ChatViewHolder>() {

    private var displayedList: List<DmMessages?>? = null
        inner class ChatViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root){
            fun bindItem(character: DmMessages){
                binding.textViewName.text = character.sender
                binding.textViewSpecie.text = character.message
                binding.textViewStatus.text = character.time

                binding.dmRoot.setOnClickListener {
//                    context.findNavController().navigate(R.id.direct_messages_screen)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
            return ChatViewHolder(ListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false))
        }

        override fun getItemCount(): Int {
            return characters.size
        }

        override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
            val cat = characters[position]
            return holder.bindItem(cat)
        }
    fun updateList(list: List<DmMessages?>) {
        displayedList = list
        notifyDataSetChanged()
    }

    fun filter(text: CharSequence) {
        val temp: MutableList<DmMessages> = ArrayList()
        for (d in displayedList!!) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d != null) {
                if (d.sender.contains(text)) {
                    temp.add(d)
                }
            }
        }
        //update recyclerview
      updateList(temp)
    }

}