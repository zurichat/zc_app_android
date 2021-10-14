package com.zurichat.app.ui.newchannel.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentSelectNewChannelBinding
import com.zurichat.app.models.OrganizationMember
import com.zurichat.app.models.User
import com.zurichat.app.ui.adapters.NewChannelAdapter
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponseItem
import com.zurichat.app.ui.newchannel.SelectNewChannelViewModel
import com.zurichat.app.ui.newchannel.states.SelectNewChannelViewState
import com.zurichat.app.util.Result
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SelectNewChannelFragment : Fragment(R.layout.fragment_select_new_channel) {
    private val binding by viewBinding(FragmentSelectNewChannelBinding::bind)
    var user: OrganizationMember?= null
    private lateinit var organizationID: String

    lateinit var userList: List<OrganizationMember>
    private val adapter = NewChannelAdapter(this).also {
        it.itemClickListener = { member ->
            lifecycleScope.launch {
                val result = viewModel.createRoom(member.id)
                if(result is Result.Success)
                    findNavController().navigate(R.id.dmFragment,
                        bundleOf(
                            "room" to RoomsListResponseItem(result.data.id, org_id = result.data.orgId, room_user_ids = result.data.roomUserIds),
                            "USER" to User("","","","","","","",0,"","","")
                        ))
                else Toast.makeText(requireContext(),
                    (result as Result.Error).error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    private  val viewModel: SelectNewChannelViewModel by viewModels()
    private var endPointLoadingState: SelectNewChannelViewState<String> = SelectNewChannelViewState.Empty

    lateinit var sharedPref: SharedPreferences
    private val PREFS_NAME = "ORG_INFO"
    private val ORG_NAME = "org_name"
    private val ORG_ID = "org_id"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        organizationID = sharedPref.getString(ORG_ID, null).toString()

        setUpViews()
        initAdapter()
        observeUsersList()
        try {
            viewModel.orgID.value = organizationID
            viewModel.getListOfUsers(organizationID)
        }catch (e : Exception){
            e.printStackTrace()
        }
        observeEndPointLoading()
    }

    private fun setUpViews() {
        with(binding) {
            memberButton.setOnClickListener {
                try {
                    findNavController().navigate(R.id.action_selectNewChannelFragment_to_selectMemberFragment, bundleOf(Pair("USER_LIST",userList)))
                } catch (exc: Exception) {

                }
            }

            memberLabel.setOnClickListener {
                try {
                    findNavController().navigate(R.id.action_selectNewChannelFragment_to_selectMemberFragment,
                        bundleOf(Pair("USER_LIST",userList)))
                } catch (exc: Exception) {

                }
            }

            toolbar.setNavigationOnClickListener {
               findNavController().popBackStack()
            }

            userListProgressBar.visibility = View.VISIBLE

            val search = toolbar.menu[0]
            val searchView = search.actionView as SearchView

            searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    val filter = adapter.filter
                    filter.filter(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val filter = adapter.filter
                    filter.filter(newText)
                    return true
                }

            })
        }
    }

    private fun initAdapter() {
        binding.recyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun observeUsersList() {
        lifecycleScope.launchWhenStarted {
            viewModel._users.collect {
                when(it) {
                    is SelectNewChannelViewState.Success -> {
                        adapter.list = it.data
                        adapter.notifyDataSetChanged()
                        userList = it.data
                        binding.userListProgressBar.visibility = View.GONE
                        binding.toolbar.subtitle = "${it.data.size} ${getString(R.string.members)}"
                    }
                    is SelectNewChannelViewState.Error -> {
                        binding.userListProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(),"${it.error}", Toast.LENGTH_LONG).show()
                    }
                    is SelectNewChannelViewState.Empty -> {
                        binding.userListProgressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun observeEndPointLoading() {
        lifecycleScope.launchWhenStarted {
            viewModel._endPointResult.collect {
                endPointLoadingState = it
                if ( it is SelectNewChannelViewState.Error) {
                    Toast.makeText(requireContext(),"Error in Loading data from server", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}
