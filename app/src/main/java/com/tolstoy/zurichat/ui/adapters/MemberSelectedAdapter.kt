package com.tolstoy.zurichat.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.databinding.ItemSelectedMembersBinding
import com.tolstoy.zurichat.models.MembersData

class MemberSelectedAdapter(private val data:(MembersData)->Unit):
    RecyclerView.Adapter<MemberSelectedAdapter.SelectedViewHolder>() {

    private var DataList = mutableListOf<MembersData>()

    inner class SelectedViewHolder(val binding: ItemSelectedMembersBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bindItem(dat: MembersData) {
            binding.imageView7.setImageResource(dat.image)
            binding.textView16.text = dat.name
            binding.imageView10.setOnClickListener {
                data(dat)
            }
        }

    }

    fun addMembers(members: List<MembersData>) {
        this.DataList.clear()
        this.DataList.addAll(members)
        notifyDataSetChanged()
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