package com.tolstoy.zurichat.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ListItemSelectMemberBinding
import com.tolstoy.zurichat.models.MembersData

import com.tolstoy.zurichat.models.User

class SelectMemberAdapter(private val user: (User) -> Unit):

    RecyclerView.Adapter<SelectMemberAdapter.SelectMemberViewModel>() {
    private var members = listOf<MembersData>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadMembers(contacts: List<MembersData>) {
        this.members = contacts
        notifyDataSetChanged()
    }

    lateinit var list: List<User>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectMemberViewModel {
        val binding = ListItemSelectMemberBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SelectMemberViewModel(binding)
    }

    override fun onBindViewHolder(holder: SelectMemberViewModel, position: Int) {
        holder.apply{
            bind(list[position])
            itemView.setOnClickListener {
                user(list[position])

            }
        }
    }


    override fun getItemCount(): Int = list.size


    inner class SelectMemberViewModel(val binding: ListItemSelectMemberBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(user: User) {
            binding.channelItemPersonNameTxt.text = if(user.first_name.isEmpty() && user.last_name.isEmpty())
                "No name"
            else "${user.first_name} ${user.last_name}"
            binding.channelItemPersonIcon.setImageResource(R.drawable.ic_kolade_icon)
            binding.channelItemMessageTxt.text = user.email
        }
    }
}