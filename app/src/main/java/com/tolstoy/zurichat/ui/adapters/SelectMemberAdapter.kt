package com.tolstoy.zurichat.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.databinding.ListItemSelectMemberBinding
import com.tolstoy.zurichat.models.MembersData
import com.tolstoy.zurichat.ui.createchannel.Contact
import com.tolstoy.zurichat.ui.newchannel.NewChannelActivity

class SelectMemberAdapter(private val memberList:(MembersData)->Unit):
    RecyclerView.Adapter<SelectMemberAdapter.SelectMemberViewModel>() {
    private var members = listOf<MembersData>()
    var selectedList = listOf<MembersData>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadMembers(contacts: List<MembersData>) {
        this.members = contacts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectMemberViewModel {
        val binding = ListItemSelectMemberBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SelectMemberViewModel(binding)
    }

    override fun onBindViewHolder(holder: SelectMemberViewModel, position: Int) {
       val membersData = members[position]
        with(holder){
            with(members[position]) {
                binding.imgSelectMember.setImageResource(this.image)
                binding.txtMemberName.text = this.name
                binding.txtMemberDescription.text = this.description

                with(holder.binding){
                    holder.binding.selectMemberLayout.setOnClickListener {
                        memberList(membersData)
                        when (adapterPosition){

                            0 -> {

//                                val intent = Intent(context, NewChannelActivity::class.java)
//                                intent.putExtra("Image", image)
//                                intent.putExtra("Name", name)
//                                intent.putExtra("Description", description)
//                                context.startActivity(intent)
                            }
                        }
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int = members.size

    inner class SelectMemberViewModel(val binding: ListItemSelectMemberBinding): RecyclerView.ViewHolder(binding.root){

    }
}