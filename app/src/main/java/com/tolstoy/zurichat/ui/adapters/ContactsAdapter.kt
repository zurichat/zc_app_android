package com.tolstoy.zurichat.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.models.User
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ItemChatBinding
import com.tolstoy.zurichat.models.OrganizationMember
import com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels.SelectContactFragment
import com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels.SelectContactFragmentDirections
import com.tolstoy.zurichat.util.changeVisibility


class ContactsAdapter(val contacts: List<OrganizationMember>):
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>(), Filterable {

    private var _contacts = contacts

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(_contacts[position])

    override fun getItemCount() = _contacts.size

    inner class ViewHolder(private var binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(member: OrganizationMember) = with(binding){
            changeVisibility(View.GONE, imageChatOnline, textChatTime, textChatUnreadMessages)
            member.also{
                textChatUsername.text = it.displayName
                textChatLastMessage.text = it.email
                Glide.with(root.context.applicationContext)
                    .load(it.imageUrl).placeholder(R.drawable.ic_kolade_icon)
                    .into(imageChatUser)
            }
            root.setOnClickListener {
                val action = SelectContactFragmentDirections
                    .actionSelectContactFragmentToDmFragment(senderId = member.id, roomId = null)
                it.findNavController().navigate(action)
            }
        }
    }

    override fun getFilter() = filter

    private val filter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            if(constraint.isNullOrBlank()) return FilterResults().apply {
                values = null
            }

            val filterList = mutableListOf<OrganizationMember>()

            for(contact in contacts) {
                if(contact.displayName.contains(constraint,true) ||
                    contact.email.contains(constraint,true)) {
                        filterList.add(contact)
                }
            }

            return FilterResults().apply {
                values = filterList.toList()
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results?.values == null){
                _contacts = contacts
                notifyDataSetChanged()
                return
            }

            _contacts = results.values as List<OrganizationMember>
            notifyDataSetChanged()
        }

    }
}
