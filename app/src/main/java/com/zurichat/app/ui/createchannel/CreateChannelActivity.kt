package com.zurichat.app.ui.createchannel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.zurichat.app.R
import com.zurichat.app.databinding.ActivityCreateChannelBinding

class CreateChannelActivity : AppCompatActivity() {

    //Material Toolbar
    private lateinit var toolBar: MaterialToolbar

    //Contact list recyclerView adapter
    private val contactListAdapter = ContactListAdapter{
        contact -> selectContact(contact)
    }
    // Selected contact recyclerView adapter
    private val contactSelectedAdapter = ContactSelectedAdapter {
        contact -> removeContact(contact)
    }
    private lateinit var selectedListOfContact: MutableLiveData<List<Contact>>

    //Recycler views
    private lateinit var selectedContactsRecyclerView:RecyclerView
    private lateinit var contactListRecyclerView: RecyclerView
    private lateinit var binding: ActivityCreateChannelBinding
    private lateinit var selectedContacts:MutableList<Contact>
    private lateinit var proceedFAB:FloatingActionButton
    private var totalNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateChannelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myContacts = MyContacts.data

        selectedListOfContact = MutableLiveData<List<Contact>>()
        selectedContacts = mutableListOf<Contact>()
        contactListRecyclerView = binding.contactList
        selectedContactsRecyclerView = binding.selectedContacts
        proceedFAB = binding.fabProceed

        proceedFAB.setOnClickListener {

        }

        toolBar = binding.createChannelToolbar
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener {
            onBackPressed()
        }

        totalNumber = myContacts.size
        toolBar.title = "Select Contact"
        toolBar.subtitle = "$totalNumber Contacts"

        selectedContactsRecyclerView.adapter = contactSelectedAdapter
        contactListRecyclerView.adapter = contactListAdapter

        contactListAdapter.loadContacts(myContacts)
        setupLiveData()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.search -> {
                true
            }
            else -> onOptionsItemSelected(item)
        }
    }

    /* Setup Live data to observe selected contacts so as to
    trigger the Toolbar title and subtitle and floating ActionButton
     */
    private fun setupLiveData(){
        selectedListOfContact.observe(this) {

            contactListAdapter.selectedList =selectedContacts
            contactListAdapter.notifyDataSetChanged()

            if (it.isEmpty()) {
                selectedContactsRecyclerView.visibility = View.GONE
                toolBar.title = "Select Members"
                toolBar.subtitle = "Choose channel members"
                proceedFAB.visibility = View.GONE

            } else {
                selectedContactsRecyclerView.visibility = View.VISIBLE
                contactSelectedAdapter.loadContacts(selectedContacts)

                // selected contact recyclerview scroll to the last item
                selectedContactsRecyclerView.smoothScrollToPosition(selectedContacts.size-1)
                toolBar.title = "New Channel"
                val selectedNumber = selectedContacts.size
                toolBar.subtitle = "$selectedNumber out of $totalNumber"
                proceedFAB.visibility = View.VISIBLE
            }

            contactSelectedAdapter.loadContacts(selectedContacts)
        }
    }


    //function to add contact to contact list
    private fun selectContact(contact: Contact){
        binding.newChannelLayout.visibility = View.GONE
        if(!selectedContacts.contains(contact)){
            selectedContacts.add(contact)
            selectedListOfContact.value = selectedContacts
        }
        else{
            removeContact(contact)
        }

    }

    //Remove Contact from Selected List
    private fun removeContact(contact: Contact){
        selectedContacts.remove(contact)
        selectedListOfContact.value = selectedContacts
    }


}