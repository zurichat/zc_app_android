package com.zurichat.app.ui.dm_chat.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.zurichat.app.databinding.FragmentDmBinding
import com.zurichat.app.models.User
import com.zurichat.app.ui.add_channel.BaseItem
import com.zurichat.app.ui.add_channel.BaseListAdapter
import com.zurichat.app.ui.dm_chat.model.request.SendMessageBody
import com.zurichat.app.ui.dm_chat.model.response.message.BaseRoomData
import com.zurichat.app.ui.dm_chat.model.response.message.Data
import com.zurichat.app.ui.dm_chat.model.response.message.SendMessageResponse
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponseItem
import com.zurichat.app.ui.dm_chat.repository.Repository
import com.zurichat.app.ui.dm_chat.viewmodel.RoomViewModel
import com.zurichat.app.ui.dm_chat.viewmodel.RoomViewModelFactory
import com.zurichat.app.ui.fragments.channel_chat.ChannelHeaderItem
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates
import kotlin.random.Random


class RoomFragment : Fragment() {

    private lateinit var roomsListAdapter : BaseListAdapter
    private lateinit var roomId: String
    private lateinit var userId: String
    private lateinit var senderId: String
    private lateinit var user : User
    private lateinit var room : RoomsListResponseItem
    private var currentPosition: Int? = null
    private lateinit var roomMsgViewModel: RoomViewModel
    private lateinit var binding: FragmentDmBinding

    private val PREFS_NAME = "ORG_INFO"
    private val ORG_NAME = "org_name"
    private val ORG_ID = "org_id"
    private val MEM_ID = "mem_Id"
    private lateinit var sharedPref: SharedPreferences
    private lateinit var organizationID: String
    private lateinit var memberId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDmBinding.inflate(inflater, container, false)
        val bundle1 = arguments
        user = bundle1?.getParcelable("USER")!!
        room = bundle1.getParcelable("room")!!
        currentPosition = bundle1.getInt("position")
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        organizationID = sharedPref.getString(ORG_ID, null).toString()
        memberId = sharedPref.getString(MEM_ID, null).toString()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val channelChatEdit = binding.channelChatEditText           //get message from this edit text
        val sendVoiceNote = binding.sendVoiceBtn
        val sendMessage = binding.sendMessageBtn                    //use this button to send the message
        val typingBar = binding.channelTypingBar
        val toolbar = binding.toolbarDm

        roomId = room.room_name
        userId = room.room_user_ids.first()
        senderId = room.room_user_ids.last()

        toolbar.title = roomId

        channelChatEdit.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                sendMessage.isEnabled = false
                sendVoiceNote.isEnabled = true
            } else {
                sendMessage.isEnabled = true
                sendVoiceNote.isEnabled = false
            }
        }

        roomsListAdapter = BaseListAdapter {
        }

        binding.listDm.adapter = roomsListAdapter
        binding.listDm.itemAnimator = null

        val repository = Repository()
        val viewModelFactory = RoomViewModelFactory(repository)
        roomMsgViewModel = ViewModelProvider(this, viewModelFactory).get(RoomViewModel::class.java)
        roomMsgViewModel.getMessages(organizationID, roomId)

        roomMsgViewModel.myGetMessageResponse.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                val messageResponse = response.body()
                messageResponse?.results?.forEach{
                    val newBaseRoomData = BaseRoomData(it, null, true)
                    messagesArrayList.add(newBaseRoomData)
                }
                createMessagesList(messagesArrayList).let {
                    roomsListAdapter.submitList(it)
                }

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
        })


        sendMessage.setOnClickListener{
            if (channelChatEdit.text.toString().isNotEmpty()){
                val s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                s.timeZone = TimeZone.getTimeZone("UTC")
                val time = s.format(Date(System.currentTimeMillis()))

                val message = channelChatEdit.text.toString()
                val dataMessage = Data(time, message, senderId)
                val sendMessageResponse = SendMessageResponse(dataMessage, "message_create", generateID().toString(), roomId, "201", false)
                val baseRoomData = BaseRoomData(null, sendMessageResponse, false)
                messagesArrayList.add(baseRoomData)
                val messagePosition: Int = messagesArrayList.size -1

                val messagesWithDateHeaders = createMessagesList(messagesArrayList).let {
                    roomsListAdapter.submitList(it)
                }
                val messageBody = SendMessageBody(message, roomId, senderId )
                roomMsgViewModel.sendMessages(organizationID, roomId, messageBody)
                roomMsgViewModel.mySendMessageResponse.observe(viewLifecycleOwner, { response ->
                    if (response.isSuccessful) {
                        val messageResponse = response.body()
                        val position = messagesArrayList.indexOf(baseRoomData)
                        val newBaseRoomData = BaseRoomData(null, messageResponse, false)
                        messagesArrayList[messagePosition] = newBaseRoomData
                        createMessagesList(messagesArrayList).let {
                            roomsListAdapter.submitList(it)
                        }
                        Log.i("Message Response", "$messageResponse")
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
                })
                channelChatEdit.text?.clear()
            }
        }

       // val instant: Instant = BING_INSTANT_PARSER.parse(stringFromBing, Instant::from)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private var messagesArrayList: ArrayList<BaseRoomData> = ArrayList()
    private fun createMessagesList(rooms: List<BaseRoomData>): MutableList<BaseItem<*>> {
        // Wrap data in list items
        val roomsItems = rooms.map {
            RoomListItem(it, user,requireActivity())
        }

        val roomsWithDateHeaders = mutableListOf<BaseItem<*>>()
        // Loop through the channels list and add headers where we need them
        var currentHeader: String? = null

        roomsItems.forEach{ c->
            if (c.data.checkMessage){
                val dateString = DateUtils.getRelativeTimeSpanString(convertStringDateToLong(c.data.getMessageResponse!!.created_at),
                    Calendar.getInstance().timeInMillis,
                    DateUtils.DAY_IN_MILLIS)
                dateString.toString().let {
                    if (it != currentHeader){
                        roomsWithDateHeaders.add(ChannelHeaderItem(it))
                        currentHeader = it
                    }
                }
                roomsWithDateHeaders.add(c)
            } else {
                val dateString = DateUtils.getRelativeTimeSpanString(convertStringDateToLong(c.data.sendMessageResponse!!.data.created_at),
                    Calendar.getInstance().timeInMillis,
                    DateUtils.DAY_IN_MILLIS)
                dateString.toString().let {
                    if (it != currentHeader){
                        roomsWithDateHeaders.add(ChannelHeaderItem(it))
                        currentHeader = it
                    }
                }
                roomsWithDateHeaders.add(c)
            }

        }

        return roomsWithDateHeaders
    }

    private fun convertStringDateToLong(date: String) : Long {
        val s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        s.timeZone = TimeZone.getTimeZone("UTC")
        var d = s.parse(date)
        return d.time
    }
    private fun generateID():Int{
        return Random(6000000).nextInt()
    }

    private val BING_INSTANT_PARSER: DateTimeFormatter =
        DateTimeFormatterBuilder().appendLiteral('"')
            .append(DateTimeFormatter.ISO_INSTANT)
            .appendLiteral('"')
            .toFormatter()
}