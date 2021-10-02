package com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentChatsBinding
import com.tolstoy.zurichat.ui.fragments.home_screen.HomeScreenFragment
import com.tolstoy.zurichat.ui.fragments.home_screen.HomeScreenFragmentDirections
import com.tolstoy.zurichat.ui.fragments.home_screen.HomeScreenViewModel
import com.tolstoy.zurichat.ui.fragments.home_screen.adapters.ChatsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ChatsFragment : Fragment(R.layout.fragment_chats) {
    private lateinit var binding: FragmentChatsBinding
    private val adapter by lazy { ChatsAdapter() }
    val viewModel: HomeScreenViewModel by lazy {
        (parentFragment as HomeScreenFragment).viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChatsBinding.bind(view)
        setupObservers()
        setupUI()
    }

    private fun setupObservers() = with(viewModel){
        userRooms.observe(viewLifecycleOwner){
            getChats(it)
        }
        userChats.observe(viewLifecycleOwner){
            adapter.addItems(*it.toTypedArray())
        }
    }

    private fun setupUI() = with(binding){
        listChats.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter.also {
                it.setItemClickListener { chat ->
                    val action = HomeScreenFragmentDirections
                        .actionHomeScreenFragmentToDmFragment(chat.message.roomId,
                            chat.message.senderId)
                    requireView().findNavController().navigate(action)
                }
            }
        }

        fabAddChat.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_selectContactFragment)
        }

        viewModel.searchQuery.observe(viewLifecycleOwner){ query ->
            Timber.d("query: $query")
            val adapter = listChats.adapter as ChatsAdapter
            adapter.differ.submitList(adapter.chats.filter {
                query.lowercase() in it.sender.lowercase()
            })
        }
    }

    companion object{
        val TAG = ChatsFragment::class.simpleName
    }
}
