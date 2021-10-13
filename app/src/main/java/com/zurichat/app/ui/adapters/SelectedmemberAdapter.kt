package com.zurichat.app.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.databinding.ListItemSelectMember2Binding
import com.zurichat.app.models.OrganizationMember

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
             binding.nameOfContact.text = if(user.firstName.isEmpty() && user.lastName.isEmpty())
                 "No name"
             else "${user.firstName} ${user.lastName}"
         }
        }

}
