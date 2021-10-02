package com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSelectContactBinding
import com.tolstoy.zurichat.ui.adapters.ContactsAdapter
import com.tolstoy.zurichat.ui.fragments.home_screen.HomeScreenViewModel
import com.tolstoy.zurichat.util.setClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectContactFragment : Fragment(R.layout.fragment_select_contact) {

    private lateinit var binding: FragmentSelectContactBinding
    private lateinit var adapter: ContactsAdapter
    private  val viewModel: HomeScreenViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSelectContactBinding.bind(view)

        setupUI()
        setupObservers()
    }

    private fun setupUI() = with(binding){
        toolbarFSC.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        listChatsFSC.also {
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        groupAddMember.setClickListener{
            findNavController().navigate(R.id.action_selectContactFragment_to_new_channel_nav_graph)
        }

        // setup search view
        (toolbarFSC.menu[0].actionView as SearchView).setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(this@SelectContactFragment::adapter.isInitialized) {
                    adapter.filter.filter(query)
                    return true
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(this@SelectContactFragment::adapter.isInitialized) {
                    adapter.filter.filter(newText)
                    return true
                }
                return false
            }
        })
    }

    private fun setupObservers() = with(viewModel){
        members.observe(viewLifecycleOwner){
            adapter = ContactsAdapter(it.data)
            binding.listChatsFSC.adapter = adapter
            binding.toolbarFSC.subtitle = if(it.data.size != 1)
                "${it.data.size} contacts" else "${it.data.size} contact"
        }
    }
}
