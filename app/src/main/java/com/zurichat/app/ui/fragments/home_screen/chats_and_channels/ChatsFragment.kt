package com.zurichat.app.ui.fragments.home_screen.chats_and_channels

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentChatsBinding
import com.zurichat.app.models.User
import com.zurichat.app.models.organization_model.UserOrganizationModel
import com.zurichat.app.ui.dm_chat.adapter.RoomAdapter
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponse
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponseItem
import com.zurichat.app.ui.dm_chat.repository.Repository
import com.zurichat.app.ui.dm_chat.utils.ModelPreferencesManager
import com.zurichat.app.ui.dm_chat.viewmodel.RoomViewModel
import com.zurichat.app.ui.dm_chat.viewmodel.RoomViewModelFactory
import com.zurichat.app.ui.fragments.home_screen.HomeScreenFragment
import com.zurichat.app.ui.fragments.home_screen.HomeScreenViewModel
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

    private lateinit var memberList: UserOrganizationModel
    private lateinit var email: String
    private lateinit var orgId: String
    private lateinit var memId: String

    private lateinit var organizationID: String
    private lateinit var organizationName: String
    private lateinit var memberId: String
    private val PREFS_NAME = "ORG_INFO"
    private val ORG_NAME = "org_name"
    private val ORG_ID = "org_id"
    private val MEM_ID = "mem_Id"
    private lateinit var sharedPref: SharedPreferences


    private lateinit var adapt: RoomAdapter

    private lateinit var binding: FragmentChatsBinding
    val viewModel: HomeScreenViewModel by lazy {
        (parentFragment as HomeScreenFragment).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        roomsArrayList = ArrayList()
        recyclerView = binding.listChats
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatsBinding.bind(view)
        recyclerView = binding.listChats
        user = requireActivity().intent.extras?.getParcelable("USER")!!
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        organizationName = sharedPref.getString(ORG_NAME, null).toString()
        organizationID = sharedPref.getString(ORG_ID, null).toString()
        memberId = sharedPref.getString(MEM_ID, null).toString()

        ModelPreferencesManager.with(requireContext())
        roomsArrayList = ArrayList()
        adapt = RoomAdapter(requireActivity(), roomsArrayList)
        if (!mNotified) {
            NotificationUtils().setNotification(mNotificationTime, requireActivity())
        }

        //setup viewModel and Retrofit
        val repository = Repository()
        val viewModelFactory = RoomViewModelFactory(repository)
        viewModelRoom = ViewModelProvider(this, viewModelFactory).get(RoomViewModel::class.java)

        //call retrofit service function to get rooms
        viewModelRoom.getRooms(organizationID, memberId)
        viewModelRoom.myResponse.observe(viewLifecycleOwner) { response ->
            roomsArrayList.clear()
            if (response.isSuccessful) {
                roomList = response.body()!!
                if(roomList.isEmpty()){
                    binding.groupChatBlank.visibility = View.VISIBLE
                } else roomList.forEach{
                    roomsArrayList.add(it)
                }
                ModelPreferencesManager.put(roomList, "rooms")
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
            recyclerView.adapter = adapt
        }
        selectChatItem()
        setupUI()
    }

    private fun selectChatItem() {
        adapt.setItemClickListener {
            val position = roomsArrayList.indexOf(it)
            roomsArrayList[position] = it
            room = roomsArrayList[position]
            val bundle1 = Bundle()
            bundle1.putParcelable("USER",user)
            bundle1.putParcelable("room", room)
            bundle1.putInt("position", position)
            findNavController().navigate(R.id.dmFragment, bundle1)
        }
    }

    private fun createNewRoom() {
        val createRoomFab = binding.fabAddChat
        createRoomFab.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_createRoomFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        ModelPreferencesManager.get<RoomsListResponse>("room")

    }

    private fun setupUI() = with(binding){
//        viewModel.userRooms.observe(viewLifecycleOwner){ rooms ->
//            val chats = rooms.map { room ->
//                val otherId = room.roomUserIds.first { it != viewModel.user!!.id }
//                // TODO: Resolve the name of each chat by getting the name of the user that holds this id
//                ChatsAdapter.Chat("Mark", Message(senderId = otherId,
//                    roomId = room.id, message = "Hey what's good"), (Math.random() * 10).toInt())
//            }
//            listChats.also {
//                it.layoutManager = LinearLayoutManager(requireContext())
//                it.adapter = ChatsAdapter(chats).apply {
//                    setItemClickListener { chat ->
//                        val action = HomeScreenFragmentDirections
//                            .actionHomeScreenFragmentToDmFragment(chat.message.roomId,
//                                viewModel.user!!.id, chat.message.senderId)
//                        requireView().findNavController().navigate(action)
//                    }
//                }
//            }
//        }

        fabAddChat.setOnClickListener {
            findNavController().navigate(R.id.new_channel_nav_graph)
        }

//        viewModel.searchQuery.observe(viewLifecycleOwner){ query ->
//            Log.d(TAG, "query: $query")
//            val adapter = listChats.adapter as ChatsAdapter
//            adapter.differ.submitList(adapter.chats.filter {
//                query.lowercase() in it.sender.lowercase()
//            })
//        }
    }

    private fun setupObservers() = with(viewModel){
        getRooms()
    }

    companion object{
        val TAG = ChatsFragment::class.simpleName
    }

}
