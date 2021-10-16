package com.zurichat.app.ui.fragments.channel_chat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Parcelable
import android.text.InputType
import android.text.format.DateUtils
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.AbsListView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.collection.ArrayMap
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.gson.Gson
import com.zurichat.app.R
import com.zurichat.app.data.localSource.AppDatabase
import com.zurichat.app.data.localSource.dao.OrganizationMembersDao
import com.zurichat.app.databinding.FragmentChannelChatBinding
import com.zurichat.app.databinding.PartialAttachmentPopupBinding
import com.zurichat.app.models.ChannelModel
import com.zurichat.app.models.OrganizationMember
import com.zurichat.app.models.User
import com.zurichat.app.ui.add_channel.BaseItem
import com.zurichat.app.ui.add_channel.BaseListAdapter
import com.zurichat.app.ui.dm.MEDIA
import com.zurichat.app.ui.dm_chat.fragment.RoomFragmentDirections
import com.zurichat.app.ui.fragments.channel_chat.localdatabase.ChannelMessagesDao
import com.zurichat.app.ui.fragments.channel_chat.localdatabase.RoomDao
import com.zurichat.app.ui.fragments.channel_chat.localdatabase.RoomDataObject
import com.zurichat.app.ui.fragments.home_screen.CentrifugeClient
import com.zurichat.app.ui.fragments.model.Data
import com.zurichat.app.ui.fragments.model.JoinChannelUser
import com.zurichat.app.ui.fragments.model.RoomData
import com.zurichat.app.ui.fragments.viewmodel.ChannelMessagesViewModel
import com.zurichat.app.ui.fragments.viewmodel.ChannelViewModel
import com.zurichat.app.ui.fragments.viewmodel.SharedChannelViewModel
import com.zurichat.app.ui.notification.NotificationUtils
import com.zurichat.app.util.mapToMemberList
import com.zurichat.app.util.setClickListener
import dagger.hilt.android.AndroidEntryPoint
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions
import io.github.centrifugal.centrifuge.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.random.Random

@AndroidEntryPoint
class ChannelChatFragment : Fragment() {
    private val viewModel: ChannelViewModel by viewModels()
    private lateinit var sharedViewModel: SharedChannelViewModel
    private lateinit var binding: FragmentChannelChatBinding
    private lateinit var user: User
    private lateinit var channel: ChannelModel
    private lateinit var organizationID: String

    private var roomData: RoomData? = null
    private lateinit var database: AppDatabase
    private lateinit var roomDao: RoomDao
    private lateinit var channelMessagesDao: ChannelMessagesDao
    private var members: List<OrganizationMember> = ArrayList()
    private var messagesArrayList: ArrayList<Data> = ArrayList()
    private lateinit var emojiIconsActions: EmojIconActions

    private var channelJoined = false

    private var isEnterSend: Boolean = false

    private val mNotificationTime = Calendar.getInstance().timeInMillis + 5000 //Set after 5 seconds from the current time.
    private var mNotified = false

    private val channelMsgViewModel: ChannelMessagesViewModel by viewModels()
    private lateinit var channelListAdapter: BaseListAdapter
    private lateinit var partialAttachmentPopupBinding: PartialAttachmentPopupBinding

    @Inject
    lateinit var preference : SharedPreferences

    lateinit var sharedPref: SharedPreferences
    private val PREFS_NAME = "ORG_INFO"
    private val ORG_NAME = "org_name"
    private val ORG_ID = "org_id"

    var userMap: ArrayMap<String,OrganizationMember> = ArrayMap()
    private lateinit var organizationMembersDao: OrganizationMembersDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChannelChatBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedChannelViewModel::class.java)

        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        database = Room.databaseBuilder(requireActivity().applicationContext, AppDatabase::class.java, "zuri_chat")
            .build()
        roomDao = database.roomDao()
        channelMessagesDao = database.channelMessagesDao()
        organizationMembersDao = database.organizationMembersDao()
        isEnterSend = PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean("enter_to_send", false)

        job = Job()
        uiScope = CoroutineScope(Dispatchers.Main + job)

        val bundle = arguments
        if (bundle != null) {
            user = bundle.getParcelable("USER")!!
            channel = bundle.getParcelable("Channel")!!
            channelJoined = bundle.getBoolean("Channel Joined")
            bundle.get("members").let {
                if(it!=null){

                    members = it as List<OrganizationMember>

                }
            }

            organizationID = sharedPref.getString(ORG_ID, "") ?: ""
            uiScope.launch(Dispatchers.IO) {
                roomDao.getRoomDataWithChannelID(channel._id).let {
                    uiScope.launch(Dispatchers.Main) {
                        if (it != null) {
                            roomData = RoomData(it.channelId, it.socketName)
                            connectToSocket()
                        } else {
                            channelMsgViewModel.retrieveRoomData(organizationID, channel._id)
                        }
                    }
                }
            }
        }

        partialAttachmentPopupBinding = PartialAttachmentPopupBinding.inflate(inflater, container, false)

        return binding.root
    }

    lateinit var toolbar: Toolbar
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // code to control the dimming of background
        val prefMngr = PreferenceManager.getDefaultSharedPreferences(context)
        val dimVal = prefMngr.getInt("bar", 50).toFloat().div(100f)

        val dimmerBox = binding.dmChatDimmer
        val channelChatEdit =
            binding.channelChatEditText           //get message from this edit text
        val sendVoiceNote = binding.sendVoiceBtn
        val sendMessage =
            binding.sendMessageBtn                    //use this button to send the message
        val typingBar = binding.cardView
        toolbar = view.findViewById(R.id.channel_toolbar)

        val imagePicker = ImagePicker(this)

        //val includeAttach = binding.attachment
        val attachment = binding.channelLink
        val popupView: View = layoutInflater.inflate(R.layout.partial_attachment_popup, null)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        emojiIconsActions =
            EmojIconActions(context, view, binding.channelChatEditText, binding.iconBtn)
        emojiIconsActions.ShowEmojIcon()
        emojiIconsActions.addEmojiconEditTextList()


        dimmerBox.alpha = dimVal

        if (channelJoined) {
            dimmerBox.visibility = View.GONE
            binding.channelJoinBar.visibility = View.GONE
            sendMessage.visibility = View.VISIBLE
            //sendVoiceNote.visibility = View.VISIBLE
        } else {
            dimmerBox.visibility = View.VISIBLE
            binding.channelName.text = channel.name

            sendMessage.visibility = View.GONE
            //sendVoiceNote.visibility = View.GONE

            if (channel.isPrivate) {
                binding.channelName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_new_lock
                    ), null, null, null
                )
            } else {
                binding.channelName.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_hash
                    ), null, null, null
                )
            }

            binding.channelJoinBar.visibility = View.VISIBLE

            binding.joinChannel.setOnClickListener {
                binding.joinChannel.visibility = View.GONE
                binding.text2.visibility = View.GONE
                binding.channelName.visibility = View.GONE
                binding.progressBar2.visibility = View.VISIBLE
                val joinChannelUser = JoinChannelUser(user.id)
                joinChannelUser.role_id = "manager"
                viewModel.joinChannel(organizationID, channel._id, joinChannelUser)
            }

            viewModel.joinedUser.observe(viewLifecycleOwner, { joinedUser ->
                if (joinedUser != null) {
                    sendMessage.visibility = View.VISIBLE
                    //sendVoiceNote.visibility = View.VISIBLE
                    dimmerBox.visibility = View.GONE
                    toolbar.subtitle = channel.members.plus(1).toString().plus(" Members")
                    Toast.makeText(requireContext(), "Joined Channel Successfully", Toast.LENGTH_SHORT).show()
                    binding.channelJoinBar.visibility = View.GONE
                } else {
                    binding.joinChannel.visibility = View.VISIBLE
                    binding.text2.visibility = View.VISIBLE
                    binding.channelName.visibility = View.VISIBLE
                    binding.progressBar2.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.an_error_occured),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }

        toolbar.title = channel.name
        if (channel.members > 1) {
            toolbar.subtitle = channel.members.toString().plus(" Members")
        } else {
            toolbar.subtitle = channel.members.toString().plus(" Member")
        }
//        toolbar.setOnClickListener { findNavController().navigate(ChannelChatFragmentDirections.
//        actionChannelChatFragmentToChannelInfoNavFragment2()) }

        channelChatEdit.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                sendMessage.isEnabled = false
                sendVoiceNote.isEnabled = true
            } else {
                sendMessage.isEnabled = true
                sendVoiceNote.isEnabled = false
            }
        }

        //Launch Attachment Popup
        popupWindow.setBackgroundDrawable(ColorDrawable())
        popupWindow.isOutsideTouchable = true


        attachment.setOnClickListener {
            //popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 600)
            popupWindow.showAsDropDown(typingBar, 0, -(typingBar.height * 4), Gravity.TOP)
        }

        partialAttachmentPopupBinding.also {
            it.groupGallery.setClickListener { navigateToAttachmentScreen() }
            it.groupAudio.setClickListener { navigateToAttachmentScreen(MEDIA.AUDIO) }
            it.groupDocument.setClickListener { navigateToAttachmentScreen(MEDIA.DOCUMENT) }
        }

        setupKeyboard()

        channelListAdapter = BaseListAdapter {

        }

        uiScope.launch(Dispatchers.IO) {
            channelMessagesDao.getChannelMessagesWithChannelID(channel._id).let {
                uiScope.launch(Dispatchers.Main) {
                    if (it != null) {
                        messagesArrayList.clear()
                        messagesArrayList.addAll(it.data)
                        if (messagesArrayList.isNotEmpty()) {
                            val updatedList = channelMsgViewModel.getProfilePictures(
                                organizationID,
                                messagesArrayList
                            )
                            val channelsWithDateHeaders = createMessagesList(updatedList)
                            channelListAdapter.submitList(channelsWithDateHeaders)
                            binding.introGroupText.visibility = View.GONE
                            binding.recyclerMessagesList.scrollToPosition(channelsWithDateHeaders.size - 1)
                        }
                    }
                }
            }
            organizationMembersDao.getMembers(organizationID).collect {
                try{
                    it.mapToMemberList().forEach { organizationMember ->
                        userMap[organizationMember.id] = organizationMember
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
                uiScope.launch(Dispatchers.Main) {
                    if (messagesArrayList.isNotEmpty()) {
                        val channelsWithDateHeaders = createMessagesList(messagesArrayList)
                        channelListAdapter.submitList(channelsWithDateHeaders)
                    }
                }
            }
        }

        binding.recyclerMessagesList.adapter = channelListAdapter
        binding.recyclerMessagesList.itemAnimator = null

        binding.recyclerMessagesList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING){
                   /* scrollDown = if(!binding.recyclerMessagesList.canScrollVertically(1)){
                        true
                    }else{
                        false
                    }*/
                }
                if(binding.recyclerMessagesList.canScrollVertically(1)){
                    scrollDown = false
                    binding.scrollDown.visibility = View.VISIBLE
                }else{
                    scrollDown = true
                    binding.scrollDown.visibility = View.GONE
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        binding.cameraChannelBtn.setOnClickListener {
            imagePicker.pickFromStorage { imageResult ->
                when (imageResult) {
                    is ImageResult.Success -> {
                        /*val uri = imageResult.value
                       */
                    }
                    is ImageResult.Failure -> {
                        val errorString = imageResult.errorString
                        Toast.makeText(requireContext(), errorString, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.scrollDown.setOnClickListener {
            scrollDown = true
            binding.recyclerMessagesList.smoothScrollToPosition(channelListAdapter.itemCount-1)
            binding.scrollDown.visibility = View.GONE
        }

        /**
         * Retrieves the channel Id from the channelModel class to get all messages from the endpoint
         * Makes the network call from the ChannelMessagesViewModel
         */
        if (channelMsgViewModel.allMessages.value == null) {
            channelMsgViewModel.retrieveAllMessages(organizationID, channel._id)

            if (!mNotified) {
                NotificationUtils().setNotification(mNotificationTime, requireActivity())
            }
        }

        val listLiveData = channelMsgViewModel.allMessages
        // Observes result from the viewModel to be passed to an adapter to display the messages
        listLiveData.observeOnce(viewLifecycleOwner, {
            messagesArrayList.clear()
            messagesArrayList.addAll(it.data)

            if (messagesArrayList.isNotEmpty()) {
                uiScope.launch(Dispatchers.IO) {
                    it.channelId = channel._id
                    channelMessagesDao.insertAll(it)
                }
                val updatedList = channelMsgViewModel.getProfilePictures(
                    organizationID,
                    messagesArrayList
                )
                val channelsWithDateHeaders = createMessagesList(updatedList)
                channelListAdapter.submitList(channelsWithDateHeaders)
                binding.introGroupText.visibility = View.GONE
                binding.recyclerMessagesList.scrollToPosition(channelsWithDateHeaders.size - 1)
            }

            channelMsgViewModel.allMessages.observe(viewLifecycleOwner,{ allChannelMessages ->
                messagesArrayList.clear()
                messagesArrayList.addAll(allChannelMessages.data)

                if (messagesArrayList.isNotEmpty()) {
                    uiScope.launch(Dispatchers.IO) {
                        allChannelMessages.channelId = channel._id
                        channelMessagesDao.insertAll(allChannelMessages)
                    }
                    val updatedList = channelMsgViewModel.getProfilePictures(
                        organizationID,
                        messagesArrayList
                    )
                    val channelsWithDateHeaders = createMessagesList(updatedList)
                    channelListAdapter.submitList(channelsWithDateHeaders)
                    binding.introGroupText.visibility = View.GONE
                    if (scrollDown){
                        binding.recyclerMessagesList.scrollToPosition(channelsWithDateHeaders.size - 1)
                    }else{
                        binding.scrollDown.visibility = View.VISIBLE
                    }
                }
            })
        })

        channelMsgViewModel.newMessage.observe(viewLifecycleOwner, {
            /*if (messagesArrayList.contains(it)) {
                val pos = messagesArrayList.indexOf(it)
                messagesArrayList[pos] = it
                val updatedList = channelMsgViewModel.getProfilePictures(organizationID, messagesArrayList)
                val channelsWithDateHeaders = createMessagesList(updatedList)
                channelListAdapter.submitList(channelsWithDateHeaders)
            } else {

            }*/
            messagesArrayList.remove(it)
            messagesArrayList.add(it)
            val updatedList = channelMsgViewModel.getProfilePictures(organizationID, messagesArrayList)
            val channelsWithDateHeaders = createMessagesList(updatedList)
            channelListAdapter.submitList(channelsWithDateHeaders)
            if (scrollDown) {
                lifecycleScope.launch {
                    delay(100)
                    //binding.recyclerMessagesList.scrollToPosition(channelsWithDateHeaders.size-1)
                    binding.recyclerMessagesList.smoothScrollToPosition(channelsWithDateHeaders.size - 1)
                }
            }else{
                binding.scrollDown.visibility = View.VISIBLE
            }
            channelChatEdit.setText("")
        })

        channelMsgViewModel.roomData.observe(viewLifecycleOwner, {
            if (it != null) {
                roomData = it
                val roomDataObject = RoomDataObject()
                roomDataObject.channelId = roomData!!.channel_id
                roomDataObject.socketName = roomData!!.socket_name

                uiScope.launch(Dispatchers.IO) {
                    roomDao.insertAll(roomDataObject)
                }
                connectToSocket()
            }
        })

        sendMessage.setOnClickListener {
            if (channelChatEdit.text.toString().isNotEmpty()) {
                val s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                s.timeZone = TimeZone.getTimeZone("UTC")
                val time = s.format(Date(System.currentTimeMillis()))
                val data = Data(
                    generateID().toString(),
                    false,
                    channel._id,
                    channelChatEdit.text.toString(),
                    false,
                    null,
                    null,
                    null,
                    false,
                    false,
                    0,
                    time,
                    "message",
                    user.id
                )

                channelMsgViewModel.sendMessages(
                    data,
                    organizationID,
                    channel._id,
                    messagesArrayList
                )
                channelChatEdit.text?.clear()
            }
        }

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.channel_invite -> {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.putExtra(
                        Intent.EXTRA_TEXT,
                        "https://api.zuri.chat/channels/${organizationID}/${channel._id}"
                    )
                    intent.type = "text/plain"

                    val shareIntent = Intent.createChooser(intent, null)
                    startActivity(shareIntent)
                }
                R.id.channel_info -> {
                    val memberList = members
                    val bundle = Bundle()
                    bundle.putParcelableArrayList("members", memberList as ArrayList<out Parcelable>)
                    bundle.putString("channel_name", channel.name)
                    findNavController().navigate(R.id.channel_info_nav_graph, bundle)
                }
            }
            true
        }

        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                channelMsgViewModel.retrieveAllMessages(organizationID, channel._id)
                handler.postDelayed(this,2000)
            }
        })
    }

    var scrollDown = true
    private lateinit var job: Job
    private lateinit var uiScope: CoroutineScope

    private lateinit var client: Client
    private fun connectToSocket() {
        val channelChatEdit = binding.channelChatEditText

        uiScope.launch(Dispatchers.IO) {
            try {
                if (CentrifugeClient.isConnected()){
                    CentrifugeClient.subscribeToChannel(roomData!!.socket_name)
                }
                client = CentrifugeClient.getClient(user)
                CentrifugeClient.setCustomListener(object : CentrifugeClient.ChannelListener {
                    override fun onConnected(connected: Boolean) {
                        try{
                            if(connected){
                                CentrifugeClient.subscribeToChannel(roomData!!.socket_name)
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
                    }

                    override fun onDataPublished(subscription: Subscription?, publishEvent: PublishEvent?) {
                        val dataString = String(publishEvent!!.data, StandardCharsets.UTF_8)
                        val data = Gson().fromJson(dataString, Data::class.java)
                        uiScope.launch(Dispatchers.Main) {
                           // Toast.makeText(requireContext(),data.content,Toast.LENGTH_SHORT).show()
                        }
                        if (data.channel_id == channel._id) {
                            channelMsgViewModel.receiveMessage(data)
                        }
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        binding.sendMessageBtn.setOnClickListener {
            if (channelChatEdit.text.toString().isNotEmpty()) {
                val s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                s.timeZone = TimeZone.getTimeZone("UTC")
                val time = s.format(Date(System.currentTimeMillis()))
                val data = Data(
                    generateID().toString(),
                    false,
                    channel._id,
                    channelChatEdit.text.toString(),
                    false,
                    null,
                    null,
                    null,
                    false,
                    false,
                    0,
                    time,
                    "message",
                    user.id
                )

                channelMsgViewModel.sendMessages(
                    data,
                    organizationID,
                    channel._id,
                    messagesArrayList
                )
            }
        }
    }

    private fun setupKeyboard() {
        // set keyboard to send if "enter is send" is set to true in settings
        binding.channelChatEditText.apply {
            if (isEnterSend) {
                this.inputType = InputType.TYPE_CLASS_TEXT
                this.imeOptions = EditorInfo.IME_ACTION_SEND
            }
        }

        binding.channelChatEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                // send message

                true
            } else {
                false
            }
        }
    }

    private fun navigateToAttachmentScreen(media: MEDIA = MEDIA.IMAGE) {
        findNavController().navigate(
            RoomFragmentDirections.actionDmFragmentToAttachmentsFragment(
                media
            )
        )
    }

    private fun generateID(): Int {
        return Random(6000000).nextInt()
    }

    private fun createMessagesList(channels: List<Data>): MutableList<BaseItem<*>> {
        // Wrap data in list items
        val channelsItems = channels.map {
            ChannelListItem(it, user, requireActivity(), userMap)
        }

        val channelsWithDateHeaders = mutableListOf<BaseItem<*>>()
        // Loop through the channels list and add headers where we need them
        var currentHeader: String? = null

        channelsItems.forEach { c ->
            val dateString = DateUtils.getRelativeTimeSpanString(
                convertStringDateToLong(c.data.timestamp.toString()),
                Calendar.getInstance().timeInMillis,
                DateUtils.DAY_IN_MILLIS
            )
            dateString.toString().let {
                if (it != currentHeader) {
                    channelsWithDateHeaders.add(ChannelHeaderItem(it))
                    currentHeader = it
                }
            }
            channelsWithDateHeaders.add(c)
        }

        return channelsWithDateHeaders
    }

    private fun convertStringDateToLong(date: String): Long {
        val s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        s.timeZone = TimeZone.getTimeZone("UTC")
        var d = s.parse(date)
        return d.time
    }

    private fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
        observe(owner, object : Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observer(value)
            }
        })
    }

}