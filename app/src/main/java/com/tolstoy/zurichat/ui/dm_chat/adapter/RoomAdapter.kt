package com.tolstoy.zurichat.ui.dm_chat.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.dm_chat.model.response.room.RoomsListResponse
import com.tolstoy.zurichat.ui.dm_chat.model.response.room.RoomsListResponseItem

class RoomAdapter: RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    private var myList = ArrayList<RoomsListResponseItem>()

    private var userNameNew: String = "Hamid"

    private var memId: String = ""

    inner class RoomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val username = itemView.findViewById<TextView>(R.id.text_chat_username)
        var chatPosition: Int = 0

        init {
            itemView.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false))

    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        // holder.username.text = myList[position]._id
        //memId = myList[position].room_user_ids[0].toString()

        holder.username.text = myList[position].room_user_ids[0]
        holder.chatPosition = position

    }

    override fun getItemCount(): Int {
        return myList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: RoomsListResponse) {
        myList = newList
        notifyDataSetChanged()
    }

    fun getMemId() = memId

    fun setUserName(name: String) {
        userNameNew = name
    }

}