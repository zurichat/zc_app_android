package com.zurichat.app.ui.createchannel

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.R
import com.zurichat.app.databinding.ContactListItemBinding

class ContactListAdapter(private val listener:(Contact)->Unit): RecyclerView.Adapter<ContactListAdapter.ContactViewHolder>() {
    private var contactList = listOf<Contact>()
    var selectedList = listOf<Contact>()


    @SuppressLint("NotifyDataSetChanged")
    fun loadContacts(contacts: List<Contact>) {
        this.contactList = contacts
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ContactListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.bindItem(contact)
        holder.itemView.setOnClickListener{
            listener(contact)
        }

    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    inner class ContactViewHolder(private val binding: ContactListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(contact: Contact) {
            binding.contactName.text = contact.name
            binding.contactStatus.text = contact.status
            binding.contactImage.setImageResource(R.drawable.dmuser)

            if(selectedList.contains(contact)){
                binding.checkContact.visibility = View.VISIBLE
            }
            else{
                binding.checkContact.visibility = View.GONE
            }

        }
    }
}