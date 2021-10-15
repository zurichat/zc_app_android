package com.zurichat.app.ui.fragments.home_screen.chats_and_channels

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentChatsBinding
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponseItem
import com.zurichat.app.ui.fragments.home_screen.HomeScreenFragment
import com.zurichat.app.ui.fragments.home_screen.HomeScreenViewModel
import com.zurichat.app.ui.fragments.home_screen.adapters.ChatsAdapter
import com.zurichat.app.ui.notification.NotificationUtils
import com.zurichat.app.util.observeOnce
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private val mNotificationTime = Calendar.getInstance().timeInMillis + 5000 //Set after 5 seconds from the current time.
    private var mNotified = false

    private lateinit var binding: FragmentChatsBinding
    val viewModel: HomeScreenViewModel by lazy {
        (parentFragment as HomeScreenFragment).viewModel
    }
    val adapter by lazy {
        ChatsAdapter().apply {
            onItemClickListener = { chat ->
                val bundle1 = Bundle()
                bundle1.putParcelable("USER", viewModel.user)
                bundle1.putParcelable("room",
                    RoomsListResponseItem(chat.room.id, org_id = chat.room.orgId,
                        room_user_ids = chat.room.roomUserIds, room_name = chat.sender))
                findNavController().navigate(R.id.dmFragment, bundle1)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChatsBinding.bind(view)
        setupObservers()
        setupUI()
    }

    private fun setupUI() = with(binding){
        activity?.onBackPressedDispatcher?.addCallback(requireActivity(), object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { findNavController().navigateUp() }
        })

        if (!mNotified) {
            NotificationUtils().setNotification(mNotificationTime, requireActivity())
        }

        listChats.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = adapter
        }

        fabAddChat.setOnClickListener {
            findNavController().navigate(R.id.new_channel_nav_graph)
        }
    }

    private fun setupObservers() = with(viewModel){
        getRooms()
        getMembers()
        userRooms.observeOnce(viewLifecycleOwner){ rooms ->
            members.observeOnce(viewLifecycleOwner){
                it.members?.let { members ->
                    lifecycleScope.launch {
                        adapter.addItems(rooms.map{ getChat(it, members) })
                    }
                }
            }
        }
        searchQuery.observe(viewLifecycleOwner){ query ->
            Timber.tag(TAG).d("query: $query")
            adapter.differ.also {
                it.submitList(it.currentList.filter {
                    query.lowercase() in it.sender.lowercase()
                })
            }
        }
    }

    companion object{
        val TAG = ChatsFragment::class.simpleName!!
    }
}
