package com.zurichat.app.ui.dm_chat.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.R
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponse
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponseItem

class RoomAdapter(val context: Activity, val roomList: ArrayList<RoomsListResponseItem>): RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    private var onItemClickListener: ((room: RoomsListResponseItem) -> Unit)? = null

    fun setItemClickListener(listener: (room: RoomsListResponseItem) -> Unit) {
        onItemClickListener = listener
    }

    private var myList = ArrayList<RoomsListResponseItem>()
    private var userNameNew: String = "Hamid"
    private var memId: String = ""

    inner class RoomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var currentPosition: Int = 0
        public lateinit var mRoomId: String

        fun bind(room: RoomsListResponseItem) {
           itemView.findViewById<TextView>(R.id.text_chat_username).text = room.room_user_ids[0]
            itemView.findViewById<ConstraintLayout>(R.id.root_layout).setOnClickListener{
                onItemClickListener?.invoke(room)
            }
        }
        val username = itemView.findViewById<TextView>(R.id.text_chat_username)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false))

    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {

        holder.bind(roomList[position])
//        holder.currentPosition = position
//        val room = myList.get(position)
//        holder.mRoomId = room._id

        //holder.username.text = myList[position].room_user_ids[0]

    }

    override fun getItemCount(): Int {
        return roomList.size
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