package com.zurichat.app.ui.dm_chat.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room.databaseBuilder
import com.google.gson.Gson
import com.zurichat.app.R
import com.zurichat.app.data.localSource.AppDatabase
import com.zurichat.app.data.localSource.dao.RoomMessageDao
import com.zurichat.app.databinding.FragmentDmBinding
import com.zurichat.app.databinding.PartialAttachmentPopupBinding
import com.zurichat.app.models.RoomModel
import com.zurichat.app.models.User
import com.zurichat.app.ui.add_channel.BaseItem
import com.zurichat.app.ui.add_channel.BaseListAdapter
import com.zurichat.app.ui.dm.MEDIA
import com.zurichat.app.ui.dm_chat.model.request.SendMessageBody
import com.zurichat.app.ui.dm_chat.model.response.message.BaseRoomData
import com.zurichat.app.ui.dm_chat.model.response.message.Data
import com.zurichat.app.ui.dm_chat.model.response.message.SendMessageResponse
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponseItem
import com.zurichat.app.ui.dm_chat.repository.Repository
import com.zurichat.app.ui.dm_chat.viewmodel.RoomViewModel
import com.zurichat.app.ui.dm_chat.viewmodel.RoomViewModelFactory
import com.zurichat.app.ui.fragments.channel_chat.ChannelHeaderItem
import com.zurichat.app.ui.fragments.home_screen.CentrifugeClient
import com.zurichat.app.util.isInternetAvailable
import com.zurichat.app.util.setClickListener
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions
import io.github.centrifugal.centrifuge.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random


class RoomFragment : Fragment() {
    private lateinit var roomsListAdapter : BaseListAdapter
    private lateinit var roomId: String
    private lateinit var userId: String
    private lateinit var senderId: String
    private lateinit var roomName: String
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

    private lateinit var job: Job
    private lateinit var uiScope: CoroutineScope

    private lateinit var emojiIconsActions: EmojIconActions
    private lateinit var partialAttachmentPopupBinding: PartialAttachmentPopupBinding

    private lateinit var database: AppDatabase
    private lateinit var roomMessageDao: RoomMessageDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDmBinding.inflate(inflater, container, false)

        job = Job()
        uiScope = CoroutineScope(Dispatchers.Main + job)

        val bundle1 = arguments
        user = bundle1?.getParcelable("USER")!!
        room = bundle1.getParcelable("room")!!
        currentPosition = bundle1.getInt("position")
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        organizationID = sharedPref.getString(ORG_ID, null).toString()
        memberId = sharedPref.getString(MEM_ID, null).toString()

        partialAttachmentPopupBinding =
            PartialAttachmentPopupBinding.inflate(inflater, container, false)

        database = databaseBuilder(requireActivity().applicationContext, AppDatabase::class.java, "zuri_chat")
            .build()
        roomMessageDao = database.roomMessageDao()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val channelChatEdit = binding.channelChatEditText           //get message from this edit text
        val sendVoiceNote = binding.sendVoiceBtn
        val sendMessage = binding.sendMessageBtn                    //use this button to send the message
        val typingBar = binding.channelTypingBar
        val toolbar = binding.toolbarDm
        val recyclerView = binding.listDm

        roomId = room._id
        userId = room.room_user_ids.first()
        senderId = room.room_user_ids.last()
        roomName = room.room_name

        toolbar.title = roomName

        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack(R.id.main_nav, false)
        }

        channelChatEdit.doOnTextChanged { text, start, count, after  ->
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
                messagesArrayList.clear()
                val messageResponse = response.body()
                Log.i("Message Body", messageResponse?.results?.get(0).toString())
                var x = 0
                messageResponse?.results?.forEach{
                    if (it.sender_id == senderId){
                        x++
                        val data = Data(it.created_at,it.message,it.sender_id)
                        val sendMessageResponse = SendMessageResponse(data,"",x.toString(),it.room_id,"",false)
                        val newBaseRoomData = BaseRoomData(null, sendMessageResponse, true)
                        messagesArrayList.add(newBaseRoomData)
                    }else{
                        val newBaseRoomData = BaseRoomData(it, null, false)
                        messagesArrayList.add(newBaseRoomData)
                    }
                }
                messagesArrayList.reverse()
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

        emojiIconsActions = EmojIconActions(context, view, binding.channelChatEditText, binding.iconBtn)
        emojiIconsActions.ShowEmojIcon()
        emojiIconsActions.addEmojiconEditTextList()

        partialAttachmentPopupBinding.also {
            it.groupGallery.setClickListener { navigateToAttachmentScreen() }
            it.groupAudio.setClickListener { navigateToAttachmentScreen(MEDIA.AUDIO) }
            it.groupDocument.setClickListener { navigateToAttachmentScreen(MEDIA.DOCUMENT) }
        }

        sendMessage.setOnClickListener {
            if (channelChatEdit.text.toString().isNotEmpty()) {
                //val s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                //s.timeZone = TimeZone.getTimeZone("UTC")
                //val time = s.format(Date(System.currentTimeMillis()))

                val message = channelChatEdit.text.toString()
                /*val dataMessage = Data(time, message, senderId)
                val sendMessageResponse = SendMessageResponse(dataMessage, "message_create", generateID().toString(), roomId, "201", false)
                val baseRoomData = BaseRoomData(null, sendMessageResponse, true)
                messagesArrayList.add(baseRoomData)
                val messagePosition: Int = messagesArrayList.size -1

                val messagesWithDateHeaders = createMessagesList(messagesArrayList).let {
                    roomsListAdapter.submitList(it)
                }*/
                val messageBody = SendMessageBody(message, roomId, senderId)
                roomMsgViewModel.sendMessages(organizationID, roomId, messageBody)
                channelChatEdit.text?.clear()
            }


//            val handler = Handler(Looper.getMainLooper())
//            handler.post(object : Runnable {
//                override fun run() {
//                    roomMsgViewModel.getMessages(organizationID, roomId)
//                    handler.postDelayed(this,10000)
//                }
//            })
        }

        roomMsgViewModel.mySendMessageResponse.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                val messageResponse = response.body()
                ///val position = messagesArrayList.indexOf(baseRoomData)
                val newBaseRoomData = BaseRoomData(null, messageResponse, true)
                //messagesArrayList[messagePosition] = newBaseRoomData
                messagesArrayList.remove(newBaseRoomData)
                messagesArrayList.add(newBaseRoomData)
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

        recyclerView.addOnLayoutChangeListener(View.OnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                recyclerView.postDelayed(Runnable {
                    if (recyclerView.adapter?.itemCount!! > 1){
                    recyclerView.adapter?.itemCount?.minus(1)?.let { it1 ->
                        recyclerView.smoothScrollToPosition(
                            it1
                        )
                    }
                }
                }, 100)
            }
        })

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    if(!recyclerView.canScrollVertically(1)){
                        scrollDown = true
                    }else{
                        scrollDown = false
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        connectToSocket()
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
            } else {
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
            }

        }

        return roomsWithDateHeaders
    }

    var scrollDown = true
    private lateinit var client: Client
    private fun connectToSocket() {
        uiScope.launch(Dispatchers.IO) {
            try {
                if (CentrifugeClient.isConnected()){
                    CentrifugeClient.subscribeToCentrifugoRoom(roomId)
                }
                client = CentrifugeClient.getClient(user)
                //client.connect()
                CentrifugeClient.setCentrifugoRoomListener(object : CentrifugeClient.CentrifugoRoomListener {
                    override fun onConnected(connected: Boolean) {
                        try{
                            if(connected){
                                CentrifugeClient.subscribeToCentrifugoRoom(roomId)
                            }
                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                    }

                    override fun onConnectError(client: Client?, event: ErrorEvent?) {

                    }

                    override fun onChannelSubscribed(isSubscribed: Boolean, subscription: Subscription?) {

                    }

                    override fun onChannelSubscriptionError(subscription: Subscription?, event: SubscribeErrorEvent?) {
                        if (event != null) {
                            Log.i("Room",event.message)
                        }
                    }

                    override fun onDataPublished(subscription: Subscription?, publishEvent: PublishEvent?) {
                        val dataString = String(publishEvent!!.data, StandardCharsets.UTF_8)
                        try {
                            val data = Gson().fromJson(dataString, SendMessageResponse::class.java)
                            if (data.room_id == roomId){

                            }
                        }catch (e : Exception){
                            e.printStackTrace()
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun convertStringDateToLong(date: String): Long {
        val s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        s.timeZone = TimeZone.getTimeZone("UTC")
        var d = s.parse(date)
        return d.time
    }

    private fun generateID(): Int {
        return Random(6000000).nextInt()
    }

    private fun navigateToAttachmentScreen(media: MEDIA = MEDIA.IMAGE) {
        findNavController().navigate(
            RoomFragmentDirections.actionDmFragmentToAttachmentsFragment(
                media
            )
        )
    }

    private val BING_INSTANT_PARSER: DateTimeFormatter =
        DateTimeFormatterBuilder().appendLiteral('"')
            .append(DateTimeFormatter.ISO_INSTANT)
            .appendLiteral('"')
            .toFormatter()

    private fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
        observe(owner, object : Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observer(value)
            }
        })
    }

    override fun onResume() {
        super.onResume()

        if (!requireActivity().isInternetAvailable()){
            val userID = "1"
            val senderID = "2"

            getMessage(senderID).observe(viewLifecycleOwner, {
                val senderMessage = it
                println("Sender: " + senderMessage)
            })

            getMessage(userID).observe(viewLifecycleOwner, {
                val userMessage = it
                println("User: " + userMessage)
            })
        }
    }

    override fun onPause() {
        super.onPause()

        val message = listOf(
            RoomModel("1", "Mano", "This is a message", "12:45PM"),
            RoomModel("2", "Mano", "This is a message", "12:45PM"),
            RoomModel("1", "Mano", "This is a message", "12:45PM"),
            RoomModel("2", "Mano", "This is a message", "12:45PM")
        )

        saveMessage(message)
    }

    private fun saveMessage(roomMessage: List<RoomModel>){
        lifecycleScope.launch {
            roomMessageDao.insertAll(roomMessage)
        }
    }

    fun getMessage(user_id: String) : LiveData<List<RoomModel>>{
        return roomMessageDao.getRoomMessageWithUserID(user_id)
    }
}