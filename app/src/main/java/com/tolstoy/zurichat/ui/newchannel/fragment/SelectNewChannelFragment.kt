package com.tolstoy.zurichat.ui.newchannel.fragment

import android.os.Bundle
import android.util.Log
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
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSelectNewChannelBinding
import com.tolstoy.zurichat.models.OrganizationMember
import com.tolstoy.zurichat.ui.adapters.NewChannelAdapter
import com.tolstoy.zurichat.ui.newchannel.SelectNewChannelViewModel
import com.tolstoy.zurichat.ui.newchannel.states.SelectNewChannelViewState
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SelectNewChannelFragment : Fragment(R.layout.fragment_select_new_channel) {

    private val binding by viewBinding(FragmentSelectNewChannelBinding::bind)
    var user: OrganizationMember?= null
    lateinit var userList: List<OrganizationMember>
    private val adapter = NewChannelAdapter(this)
    private  val viewModel: SelectNewChannelViewModel by viewModels()
    private var endPointLoadingState: SelectNewChannelViewState<String> = SelectNewChannelViewState.Empty

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setUpViews()
        initAdapter()
        observeUsersList()
        viewModel.getListOfUsers()
        observeEndPointLoading()

    }
    private fun setUpViews() {
        with(binding) {
            memberButton.setOnClickListener {
                try {
                    findNavController().navigate(R.id.action_selectNewChannelFragment_to_selectMemberFragment,
                        bundleOf(Pair("USER_LIST",userList)))
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
                requireActivity().finish()
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
                        binding.numberOfContactsTxt.text =
                            "${it.data.size} ${getString(R.string.members)}"
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
                    Toast.makeText(requireContext(),"Error in Loading data from server", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

    }
}
