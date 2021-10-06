package com.zurichat.app.ui.fragments.home_screen.chats_and_channels

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentChatsBinding
import com.zurichat.app.models.Message
import com.zurichat.app.models.User
import com.zurichat.app.ui.dm_chat.adapter.RoomAdapter
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponse
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponseItem
import com.zurichat.app.ui.dm_chat.repository.Repository
import com.zurichat.app.ui.dm_chat.utils.ModelPreferencesManager
import com.zurichat.app.ui.dm_chat.viewmodel.RoomViewModel
import com.zurichat.app.ui.dm_chat.viewmodel.RoomViewModelFactory
import com.zurichat.app.ui.fragments.home_screen.HomeScreenFragment
import com.zurichat.app.ui.fragments.home_screen.HomeScreenFragmentDirections
import com.zurichat.app.ui.fragments.home_screen.HomeScreenViewModel
import com.zurichat.app.ui.fragments.home_screen.adapters.ChatsAdapter
import com.zurichat.app.ui.notification.NotificationUtils
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private lateinit var user : User
    private var token: String? = null
    private lateinit var userID: String

    private val mNotificationTime = Calendar.getInstance().timeInMillis + 5000 //Set after 5 seconds from the current time.
    private var mNotified = false

    //variables initialization for new setup
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModelRoom: RoomViewModel

    private lateinit var room: RoomsListResponseItem
    private lateinit var roomList: RoomsListResponse
    private lateinit var roomsArrayList: ArrayList<RoomsListResponseItem>

    private lateinit var roomUserId: List<String>
    private lateinit var memId: String
    private lateinit var userName: String


    private val roomAdapter by lazy { RoomAdapter(requireActivity(), roomsArrayList) }

    private lateinit var adapt: RoomAdapter

    private lateinit var binding: FragmentChatsBinding
    val viewModel: HomeScreenViewModel by lazy {
        (parentFragment as HomeScreenFragment).viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatsBinding.bind(view)
        recyclerView = binding.listChats
        val recyclerView2 = view.findViewById<RecyclerView>(R.id.list_chats)
        user = requireActivity().intent.extras?.getParcelable("USER")!!

        ModelPreferencesManager.with(requireContext())
        roomsArrayList = ArrayList()

        if (!mNotified) {
            NotificationUtils().setNotification(mNotificationTime, requireActivity())
        }

        setupRecyclerView()

        val repository = Repository()
        val viewModelFactory = RoomViewModelFactory(repository)
        viewModelRoom = ViewModelProvider(this, viewModelFactory).get(RoomViewModel::class.java)
        //call retrofit service function
        viewModelRoom.getRooms()

        viewModelRoom.myResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {

                roomList = response.body()!!

                Log.i("RoomList response", "$roomList")

                // ModelPreferencesManager.put(roomList, "rooms")
                ModelPreferencesManager.put(roomList, "rooms")

                room = roomList[0]
                //  roomsArrayList.addAll(roomList)
                roomAdapter.setData(roomList)

                for (room in roomList) {
                    roomsArrayList.add(room)
                }
                addHeaders()

                Log.i("Rooms List", "$roomsArrayList")

            } else {

                when (response.code()) {
                    400 -> {
                        Log.e("Error 400", "invalid authorization")
                    }
                    404 -> {
                        Log.e("Error 404", "Not Found")
                    }
                    401 -> {
                        Log.e("Error 401", "No authorization or session expired")
                    }
                    else -> {
                        Log.e("Error", "Generic Error")
                    }
                }

            }
        }


        userID = "61467ee61a5607b13c00bcf2"
        //setupObservers()
        //setupUI()
    }

    //setup recyclerView
    private fun setupRecyclerView() {
        //recyclerView.adapter = roomAdapter
        adapt = RoomAdapter(requireActivity(), roomsArrayList)
        recyclerView.adapter = adapt
    }

    fun addHeaders() {
        //roomsArrayList.addAll(roomList)
        adapt = RoomAdapter(requireActivity(), roomsArrayList)
        recyclerView.adapter = adapt
        adapt.setItemClickListener {
            val bundle1 = Bundle()
            bundle1.putParcelable("USER",user)
            bundle1.putParcelable("room", room)
            findNavController().navigate(R.id.dmFragment, bundle1)
        }
    }

    override fun onResume() {
        super.onResume()
        ModelPreferencesManager.get<RoomsListResponse>("room")

    }

    private fun setupUI() = with(binding){
        viewModel.userRooms.observe(viewLifecycleOwner){ rooms ->
            val chats = rooms.map { room ->
                val otherId = room.roomUserIds.first { it != viewModel.user!!.id }
                // TODO: Resolve the name of each chat by getting the name of the user that holds this id
                ChatsAdapter.Chat("Mark", Message(senderId = otherId,
                    roomId = room.id, message = "Hey what's good"), (Math.random() * 10).toInt())
            }
            listChats.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = ChatsAdapter(chats).apply {
                    setItemClickListener { chat ->
                        val action = HomeScreenFragmentDirections
                            .actionHomeScreenFragmentToDmFragment(chat.message.roomId,
                                viewModel.user!!.id, chat.message.senderId)
                        requireView().findNavController().navigate(action)
                    }
                }
            }
        }

        fabAddChat.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_new_channel_nav_graph)
        }

        viewModel.searchQuery.observe(viewLifecycleOwner){ query ->
            Log.d(TAG, "query: $query")
            val adapter = listChats.adapter as ChatsAdapter
            adapter.differ.submitList(adapter.chats.filter {
                query.lowercase() in it.sender.lowercase()
            })
        }
    }

    private fun setupObservers() = with(viewModel){
        getRooms()
    }

    companion object{
        val TAG = ChatsFragment::class.simpleName
    }
}
