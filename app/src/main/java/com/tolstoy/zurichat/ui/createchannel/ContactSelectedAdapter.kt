package com.tolstoy.zurichat.ui.createchannel

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ContactSelectionItemBinding

class ContactSelectedAdapter(private val listener:(Contact)->Unit): RecyclerView.Adapter<ContactSelectedAdapter.ContactViewHolder>() {
    private var selectedContactList = mutableListOf<Contact>()



    @SuppressLint("NotifyDataSetChanged")
    fun loadContacts(contacts:List<Contact>) {
        this.selectedContactList.clear()
        this.selectedContactList.addAll(contacts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ContactSelectionItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = selectedContactList[position]
        holder.bindItem(contact)
    }

    override fun getItemCount(): Int {
        return selectedContactList.size
    }

    inner class ContactViewHolder(private val binding: ContactSelectionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(contact: Contact) {
            binding.contactImage.setImageResource(R.drawable.dmuser)
            binding.contactName.text = contact.name
            binding.removeContact.setOnClickListener{
               listener(contact)
            }
        }
    }
}