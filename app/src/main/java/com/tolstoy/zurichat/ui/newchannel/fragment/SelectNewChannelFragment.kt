package com.tolstoy.zurichat.ui.newchannel.fragment

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSelectNewChannelBinding
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.adapters.NewChannelAdapter
import com.tolstoy.zurichat.ui.newchannel.SelectNewChannelViewModel
import com.tolstoy.zurichat.ui.newchannel.states.SelectNewChannelViewState
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SelectNewChannelFragment : Fragment(R.layout.fragment_select_new_channel) {

    private val binding by viewBinding(FragmentSelectNewChannelBinding::bind)
    var user:User?= null
    lateinit var userList: List<User>
    private val adapter = NewChannelAdapter()
    private  val viewModel: SelectNewChannelViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.getListOfUsers()

        setUpViews()
        initAdapter()
        observeUsersList()

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

    private fun observeUsersList () {
        lifecycleScope.launchWhenStarted {

            viewModel._users.collect {
                when(it) {
                    is SelectNewChannelViewState.Success -> {
                        adapter.list = it.data.data
                        adapter.notifyDataSetChanged()
                        userList = it.data.data
                        binding.userListProgressBar.visibility = View.GONE
                        binding.numberOfContactsTxt.text =
                            "${it.data.data.size} ${getString(R.string.select_members_)}"
                    }
                    is SelectNewChannelViewState.Error -> {
                        binding.userListProgressBar.visibility = View.GONE
                        Toast.makeText(requireContext(),"${it.error}", Toast.LENGTH_LONG).show(


                        )
                    }
                }
            }
        }

    }
}
