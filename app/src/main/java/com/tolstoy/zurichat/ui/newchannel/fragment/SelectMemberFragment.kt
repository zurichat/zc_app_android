package com.tolstoy.zurichat.ui.newchannel

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSelectMemberBinding
import com.tolstoy.zurichat.models.MembersData
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.adapters.NewChannelAdapter
import com.tolstoy.zurichat.ui.adapters.SelectMemberAdapter
import com.tolstoy.zurichat.ui.adapters.SelectedMemberAdapter
import com.tolstoy.zurichat.util.viewBinding
import timber.log.Timber

class SelectMemberFragment : Fragment(R.layout.fragment_select_member) {


    private val binding by viewBinding(FragmentSelectMemberBinding::bind)
    private lateinit var userList: List<User>
    private val selectedUserLiveData = MutableLiveData<List<User>>()
    private val selectedUsers = mutableListOf<User>()
    private val selectMemberAdapter = SelectMemberAdapter { user -> addUser(user) }
    private val selectedMemberAdapter = SelectedMemberAdapter { user -> removeUser(user) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        userList = arguments?.get("USER_LIST") as List<User>

        with(binding) {
            //textView6.text = "${selectMember().size} Members"
            toolbar.setNavigationOnClickListener {
                try {
                    val action = SelectMemberFragmentDirections
                        .actionSelectMemberFragmentToSelectNewChannelFragment()
                    findNavController().navigate(action)
                } catch (exc: Exception) {
                    Timber.e(SelectNewChannelFragment.TAG, exc.toString())
                }
            }
            fabAddChannel.setOnClickListener {
                val action = SelectMemberFragmentDirections
                    .actionSelectMemberFragmentToNewChannelDataFragment()
                findNavController().navigate(action)
            }
        }

        initAdapter()
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
                    "${selectedUsers.size} out of ${userList.size} selected"
                binding.topRecyclerView.smoothScrollToPosition(selectedUsers.size - 1)

            }
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
    private fun addUser(user: User) {
        if (!selectedUsers.contains(user)) {
            selectedUsers.add(user)
            selectedUserLiveData.value = selectedUsers
        } else {
            removeUser(user)
        }

    }

    private fun removeUser(user: User) {
        selectedUsers.remove(user)
        selectedUserLiveData.value = selectedUsers
    }
}
