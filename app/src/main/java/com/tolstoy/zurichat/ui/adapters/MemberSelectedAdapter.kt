package com.tolstoy.zurichat.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.databinding.ItemSelectedMembersBinding
import com.tolstoy.zurichat.models.MembersData
import com.tolstoy.zurichat.ui.createchannel.Contact

class MemberSelectedAdapter(private val data: Unit):
    RecyclerView.Adapter<MemberSelectedAdapter.SelectedViewHolder>() {

    private var DataList = mutableListOf<MembersData>()
    var selectedList = listOf<MembersData>()

    inner class SelectedViewHolder(val binding: ItemSelectedMembersBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bindItem(dat: MembersData) {
            binding.imageView7.setImageResource(dat.image)
            binding.textView16.text = dat.name
        }

    }

    fun addMembers(members: List<MembersData>) {
        this.DataList.clear()
        this.DataList.addAll(members)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedViewHolder {
        return SelectedViewHolder(ItemSelectedMembersBinding.inflate(
            LayoutInflater.from(parent.context),parent, false ))
    }

    override fun onBindViewHolder(holder: SelectedViewHolder, position: Int) {
        val dat = DataList[position]
        return holder.bindItem(dat)
    }

    override fun getItemCount(): Int = DataList.size
}