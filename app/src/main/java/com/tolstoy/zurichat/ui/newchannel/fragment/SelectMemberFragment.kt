package com.tolstoy.zurichat.ui.newchannel.fragment

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSelectMemberBinding
import com.tolstoy.zurichat.models.OrganizationMember
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.adapters.SelectMemberAdapter
import com.tolstoy.zurichat.ui.adapters.SelectedMemberAdapter
import com.tolstoy.zurichat.ui.fragments.home_screen.HomeScreenViewModel

class SelectMemberFragment : Fragment(R.layout.fragment_select_member) {


    private lateinit var binding: FragmentSelectMemberBinding
    private val viewModel: HomeScreenViewModel by activityViewModels()

    private lateinit var userList: List<OrganizationMember>
    private val selectedUserLiveData = MutableLiveData<List<OrganizationMember>>()
    private val selectedUsers = mutableListOf<OrganizationMember>()
    private val selectMemberAdapter = SelectMemberAdapter { member -> addMember(member) }
    private val selectedMemberAdapter = SelectedMemberAdapter { member -> removeMember(member) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSelectMemberBinding.bind(view)
        setupUI()
        setupObservers()
    }

    private fun setupUI() = with(binding){
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
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

    private fun setupObservers() = with(viewModel){
        selectedUserLiveData.observe(viewLifecycleOwner) {

            if (it.isEmpty()) {
                binding.topRecyclerView.visibility = View.GONE
                binding.fabAddChannel.visibility = View.GONE
                binding.numberOfContactsTxt.text = "Choose Channel Members"
            } else {
                binding.topRecyclerView.visibility = View.VISIBLE
                binding.fabAddChannel.visibility = View.VISIBLE
                selectedMemberAdapter.selectedUserList = it
                selectedMemberAdapter.notifyDataSetChanged()
                binding.numberOfContactsTxt.text =
                    "${selectedUsers.size} out of ${it.size} selected"
                binding.topRecyclerView.smoothScrollToPosition(selectedUsers.size - 1)

            }
        }
        members.observe(viewLifecycleOwner){ members ->
            binding.recyclerView.also {
                it.adapter = selectMemberAdapter.apply { list =  members.data}
                it.layoutManager = LinearLayoutManager(context)
            }
            binding.topRecyclerView.also {
                it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                it.adapter = selectedMemberAdapter
            }
        }
    }

    private fun addMember(member: OrganizationMember) {
        if (!selectedUsers.contains(member)) {
            selectedUsers.add(member)
            selectedUserLiveData.value = selectedUsers
        } else {
            removeMember(member)
        }

    }

    private fun removeMember(member: OrganizationMember) {
        selectedUsers.remove(member)
        selectedUserLiveData.value = selectedUsers
    }
}
