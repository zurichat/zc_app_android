package com.tolstoy.zurichat.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.databinding.ListItemSelectMemberBinding
import com.tolstoy.zurichat.models.MembersData

class SelectMemberAdapter(private val memberList: List<MembersData>, val context: Context):
    RecyclerView.Adapter<SelectMemberAdapter.SelectMemberViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectMemberViewModel {
        val binding = ListItemSelectMemberBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SelectMemberViewModel(binding)
    }

    override fun onBindViewHolder(holder: SelectMemberViewModel, position: Int) {
       val membersData = memberList[position]
        with(holder){
            with(memberList[position]) {
                binding.imgSelectMember.setImageResource(this.image)
                binding.txtMemberName.text = this.name
                binding.txtMemberDescription.text = this.description


            }
        }
    }

    override fun getItemCount(): Int = memberList.size

    inner class SelectMemberViewModel(val binding: ListItemSelectMemberBinding): RecyclerView.ViewHolder(binding.root){

    }
}