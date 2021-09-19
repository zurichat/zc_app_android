package com.tolstoy.zurichat.ui.adapters

import android.media.MediaMetadata
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.models.MembersData

import com.tolstoy.zurichat.databinding.ItemSelectedMembersBinding


class NewChannelMemberSelectedAdapter(
    private val memberDataList: List<MembersData>,
) : RecyclerView.Adapter<NewChannelMemberSelectedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
           ItemSelectedMembersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(memberDataList[position])
    }

    override fun getItemCount(): Int = memberDataList.size

    inner class ViewHolder(private var binding: ItemSelectedMembersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(memberData: MembersData) {
            binding.imageView7.setImageResource(memberData.image)
            binding.textView16.text = memberData.name

        }
    }
}
