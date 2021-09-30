package com.tolstoy.zurichat.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.databinding.ListItemSelectMember2Binding
import com.tolstoy.zurichat.models.OrganizationMember

class SelectedMemberAdapter(private val user:(OrganizationMember) -> Unit) :
RecyclerView.Adapter<SelectedMemberAdapter.SelectedViewHolder>(){

    var selectedUserList = listOf<OrganizationMember>()

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

         fun bind(user: OrganizationMember) {
             binding.imageChatUser.setImageResource(R.drawable.ic_kolade_icon)
             binding.nameOfContact.text = if(user.firstName.isEmpty() && user.lastName.isEmpty())
                 "No name"
             else "${user.firstName} ${user.lastName}"
         }
        }

}
