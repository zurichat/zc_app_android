package com.tolstoy.zurichat.ui.newchannel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.data.remoteSource.RetrofitService
import com.tolstoy.zurichat.databinding.FragmentSelectNewChannelBinding
import com.tolstoy.zurichat.models.LoginBody
import com.tolstoy.zurichat.models.LoginResponse
import com.tolstoy.zurichat.models.NewChannel
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.activities.MainActivity
import com.tolstoy.zurichat.ui.adapters.NewChannelAdapter
import com.tolstoy.zurichat.util.viewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SelectNewChannelFragment : Fragment(R.layout.fragment_select_new_channel) {
    private val binding by viewBinding(FragmentSelectNewChannelBinding::bind)
    lateinit var userList: List<User>
    private val adapter = NewChannelAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val viewModel = ViewModelProvider.
        AndroidViewModelFactory(requireActivity().application)
            .create(SelectNewChannelViewModel::class.java)


        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            requireActivity().finish()
            //requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
        }

        with(binding) {
            memberButton.setOnClickListener {
                try {
                    findNavController().navigate(R.id.selectMemberFragment, bundleOf(Pair("USER_LIST", userList)))
                } catch (exc: Exception) {
                    Timber.e(TAG, exc.toString())
                }
            }

            memberLabel.setOnClickListener {
                try {
                    findNavController().navigate(R.id.selectMemberFragment, bundleOf(Pair("USER_LIST", userList)))
                } catch (exc: Exception) {
                    Timber.e(TAG, exc.toString())
                }
            }
        }
        initAdapter()
        viewModel.getListOfUsers().observe(viewLifecycleOwner) {
            userList = it
            adapter.list = it
            adapter.notifyDataSetChanged()
            binding.numberOfContactsTxt.text = "${it.size} Users"
        }
    }
    private fun initAdapter() {
        binding.recyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
        }
    }

    companion object {
        const val TAG = "SelectContactFragment"
    }
}
