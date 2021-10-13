package com.zurichat.app.ui.newchannel.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentSelectMemberBinding
import com.zurichat.app.models.OrganizationMember
import com.zurichat.app.ui.adapters.SelectMemberAdapter
import com.zurichat.app.ui.adapters.SelectedMemberAdapter
import com.zurichat.app.util.viewBinding

class SelectMemberFragment : Fragment(R.layout.fragment_select_member) {
    private val binding by viewBinding(FragmentSelectMemberBinding::bind)
    private lateinit var userList: List<OrganizationMember>
    private lateinit var organizationID: String
    private val selectedUserLiveData = MutableLiveData<List<OrganizationMember>>()
    private val selectedUsers = mutableListOf<OrganizationMember>()
    private val selectMemberAdapter = SelectMemberAdapter { user -> addUser(user) }
    private val selectedMemberAdapter = SelectedMemberAdapter { user -> removeUser(user) }

    lateinit var sharedPref: SharedPreferences
    private val PREFS_NAME = "ORG_INFO"
    private val ORG_NAME = "org_name"
    private val ORG_ID = "org_id"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        organizationID = sharedPref.getString(ORG_ID, null).toString()
        userList = arguments?.get("USER_LIST") as List<OrganizationMember>

        setUpViews()
        initAdapter()
        observeData()
    }

    private fun setUpViews() {
        with(binding) {
            //textView6.text = "${selectMember().size} Members"
            toolbar.setNavigationOnClickListener {
                try {
                    val action =
                        SelectMemberFragmentDirections.actionSelectMemberFragmentToSelectNewChannelFragment()
                    findNavController().navigate(action)
                } catch (exc: Exception) {

                }
            }
            fabAddChannel.setOnClickListener {
                findNavController().navigate(R.id.action_selectMemberFragment_to_newChannelDataFragment,
                    bundleOf(Pair("Selected_user", selectedUsers)))
            }

            val search = toolbar.menu[0]
            val searchView = search.actionView as SearchView

            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val filter = selectMemberAdapter.filter
                    filter.filter(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filter = selectMemberAdapter.filter
                    filter.filter(newText)
                    return true
                }

            })
        }
    }

    private fun initAdapter() {
        binding.recyclerView.also {
            it.adapter = selectMemberAdapter.apply { list = userList }
            it.layoutManager = LinearLayoutManager(context)
        }
        binding.topRecyclerView.also {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = selectedMemberAdapter
        }
    }

    private fun observeData() {
        selectedUserLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                binding.topRecyclerView.visibility = View.GONE
                binding.fabAddChannel.visibility = View.GONE
                binding.toolbar.subtitle = "Choose Channel Members"
            } else {
                binding.topRecyclerView.visibility = View.VISIBLE
                binding.fabAddChannel.visibility = View.VISIBLE
                selectedMemberAdapter.selectedUserList = it
                selectedMemberAdapter.notifyDataSetChanged()
                binding.toolbar.subtitle = "${selectedUsers.size} out of ${userList.size} selected"
                binding.topRecyclerView.smoothScrollToPosition(selectedUsers.size - 1)

            }
        }
    }

    private fun addUser(user: OrganizationMember) {
        if (!selectedUsers.contains(user)) {
            selectedUsers.add(user)
            selectedUserLiveData.value = selectedUsers
        } else {
            removeUser(user)
        }

    }

    private fun removeUser(user: OrganizationMember) {
        selectedUsers.remove(user)
        selectedUserLiveData.value = selectedUsers
    }
}
