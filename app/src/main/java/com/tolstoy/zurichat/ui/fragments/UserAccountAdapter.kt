package com.tolstoy.zurichat.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.models.User

class UserAccountAdapter: RecyclerView.Adapter<UserAccountAdapter.MyViewHolder>() {

    private var userList = emptyList<User>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.itemView.findViewById<TextView>(R.id.contactName).text= currentItem.email
        holder.itemView.findViewById<TextView>(R.id.contactStatus).text = currentItem.id
        holder.itemView.findViewById<ConstraintLayout>(R.id.contact_container).setOnClickListener {
            val action = AccountsFragmentDirections.actionAccountsFragmentToConfirmAccountPasswordFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun setData(user: List<User>){
        this.userList= user
        notifyDataSetChanged()
    }
}