package com.zurichat.app.ui.fragments.switch_account

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.R
import com.zurichat.app.models.User

class UserAccountAdapter(user: User): RecyclerView.Adapter<UserAccountAdapter.MyViewHolder>() {
    private var userList = emptyList<User>()
    private var oldUser = user

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = userList[position]
        val pos = currentItem.email.indexOf("@")
        holder.itemView.findViewById<TextView>(R.id.contactName).text= currentItem.email.substring(0,pos)
        holder.itemView.findViewById<TextView>(R.id.contactStatus).text = (currentItem.email)
        holder.itemView.findViewById<ConstraintLayout>(R.id.contact_container).setOnClickListener {
            val action = AccountsFragmentDirections.actionAccountsFragmentToConfirmAccountPasswordFragment(currentItem,oldUser)
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