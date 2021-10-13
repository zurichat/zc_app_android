package com.zurichat.app.ui.dm_chat.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.R
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponseItem
import com.zurichat.app.ui.profile.data.DataX

class CreateRoomAdapter(val context: Activity, val memberList: List<DataX>): RecyclerView.Adapter<CreateRoomAdapter.CreateRoomViewHolder>() {

    private var onItemClickListener: ((member: DataX) -> Unit)? = null
    fun setItemClickListener(listener: (member: DataX) -> Unit) {
        onItemClickListener = listener
    }
    inner class CreateRoomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(member: DataX) {
            itemView.findViewById<TextView>(R.id.member_name).text = member.display_name
            itemView.findViewById<FrameLayout>(R.id.root_layout).setOnClickListener {
                onItemClickListener?.invoke(member)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateRoomViewHolder {
       return CreateRoomViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.member_item, parent,false))
    }

    override fun onBindViewHolder(holder: CreateRoomViewHolder, position: Int) {
        holder.bind(memberList[position])
    }

    override fun getItemCount(): Int {
        return memberList.size
    }
}