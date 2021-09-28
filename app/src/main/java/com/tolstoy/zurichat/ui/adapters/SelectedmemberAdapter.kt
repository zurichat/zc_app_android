package com.tolstoy.zurichat.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.databinding.ListItemSelectMember2Binding

class SelectedMemberAdapter(private val user:(User) -> Unit) :
RecyclerView.Adapter<SelectedMemberAdapter.SelectedViewHolder>(){

    var selectedUserList = listOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedViewHolder {

        return SelectedViewHolder(ListItemSelectMember2Binding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }


    override fun onBindViewHolder(holder: SelectedViewHolder, position: Int) {
        holder.apply {
            bind(selectedUserList[position])
            itemView.setOnClickListener {
                user(selectedUserList[position])
            }

        }

    }

    override fun getItemCount() = selectedUserList.size


    inner class SelectedViewHolder(val binding: ListItemSelectMember2Binding):
        RecyclerView.ViewHolder(binding.root) {

         fun bind(user: User) {
             binding.imageChatUser.setImageResource(R.drawable.ic_kolade_icon)
             binding.nameOfContact.text = if(user.first_name.isEmpty() && user.last_name.isEmpty())
                 "No name"
             else "${user.first_name} ${user.last_name}"
         }
        }

}
