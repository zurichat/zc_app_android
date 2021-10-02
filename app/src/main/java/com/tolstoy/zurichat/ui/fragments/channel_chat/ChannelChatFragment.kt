package com.tolstoy.zurichat.ui.fragments.channel_chat

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.InputType
import android.text.format.DateUtils
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.room.Room
import centrifuge.*
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentChannelChatBinding
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.add_channel.BaseItem
import com.tolstoy.zurichat.ui.add_channel.BaseListAdapter
import com.tolstoy.zurichat.ui.fragments.model.Data
import com.tolstoy.zurichat.ui.fragments.model.JoinChannelUser
import com.tolstoy.zurichat.ui.fragments.model.RoomData
import com.tolstoy.zurichat.ui.fragments.networking.AppConnectHandler
import com.tolstoy.zurichat.ui.fragments.networking.AppDisconnectHandler
import com.tolstoy.zurichat.ui.fragments.networking.AppServerPublishHandler
import com.tolstoy.zurichat.ui.fragments.viewmodel.ChannelMessagesViewModel
import com.tolstoy.zurichat.ui.fragments.viewmodel.ChannelViewModel
import com.tolstoy.zurichat.ui.fragments.viewmodel.SharedChannelViewModel
import dev.ronnie.github.imagepicker.ImagePicker
import dev.ronnie.github.imagepicker.ImageResult
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random
import com.google.gson.Gson
import com.tolstoy.zurichat.data.localSource.AppDatabase
import com.tolstoy.zurichat.models.OrganizationMember
import com.tolstoy.zurichat.models.organization_model.OrganizationData
import com.tolstoy.zurichat.models.organization_model.UserOrganizationModel
import com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase.ChannelMessagesDao
import com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase.RoomDao
import com.tolstoy.zurichat.ui.fragments.channel_chat.localdatabase.RoomDataObject
import com.tolstoy.zurichat.ui.fragments.home_screen.CentrifugeClient
import com.tolstoy.zurichat.ui.fragments.home_screen.HomeScreenFragmentDirections
import com.tolstoy.zurichat.ui.fragments.networking.AppPublishHandler
import com.tolstoy.zurichat.ui.notification.NotificationUtils
import com.tolstoy.zurichat.ui.profile.data.DataX
import dagger.hilt.android.AndroidEntryPoint
import java.nio.charset.StandardCharsets
import javax.inject.Inject

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
    private val members = ArrayList<OrganizationMember>()

    private var channelJoined = false

    private var isEnterSend: Boolean = false

    private val mNotificationTime = Calendar.getInstance().timeInMillis + 5000 //Set after 5 seconds from the current time.
    private var mNotified = false

    private val channelMsgViewModel: ChannelMessagesViewModel by viewModels()
    private lateinit var channelListAdapter: BaseListAdapter

    @Inject
    lateinit var preference : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelChatBinding.inflate(inflater, container, false)
        sharedViewModel =
            ViewModelProvider(requireActivity()).get(SharedChannelViewModel::class.java)

        database = Room.databaseBuilder(
            requireActivity().applicationContext,
            AppDatabase::class.java,
            "zuri_chat"
        ).build()
        roomDao = database.roomDao()
        channelMessagesDao = database.channelMessagesDao()
        isEnterSend = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getBoolean("enter_to_send", false)

        job = Job()
        uiScope = CoroutineScope(Dispatchers.Main + job)

        val bundle = arguments
        if (bundle != null) {
            user = bundle.getParcelable("USER")!!
            channel = bundle.getParcelable("Channel")!!
            channelJoined = bundle.getBoolean("Channel Joined")
//            organizationID = "614679ee1a5607b13c00bcb7"
            organizationID = preference.getString("ORG_ID", "") ?: ""
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
        val typingBar = binding.channelTypingBar
        toolbar = view.findViewById<Toolbar>(R.id.channel_toolbar)

        val imagePicker = ImagePicker(this)

        //val includeAttach = binding.attachment
        val attachment = binding.channelLink
        val popupView: View = layoutInflater.inflate(R.layout.partial_attachment_popup, null)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dimmerBox.alpha = dimVal

        if (channelJoined) {
            dimmerBox.visibility = View.GONE
            binding.channelJoinBar.visibility = View.GONE
        } else {
            dimmerBox.visibility = View.VISIBLE
            binding.channelName.text = channel.name

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
                user?.let { JoinChannelUser(it.id, "manager") }
                    ?.let { viewModel.joinChannel(organizationID, channel._id, it) }
            }

            viewModel.joinedUser.observe(viewLifecycleOwner, { joinedUser ->
                if (joinedUser != null) {
                    dimmerBox.visibility = View.GONE
                    toolbar.subtitle = channel.members.plus(1).toString().plus(" Members")
                    Toast.makeText(
                        requireContext(),
                        "Joined Channel Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
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

        setupKeyboard()

        channelListAdapter = BaseListAdapter { channelItem ->

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
        }

        binding.recyclerMessagesList.adapter = channelListAdapter
        binding.recyclerMessagesList.itemAnimator = null

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
        })

        channelMsgViewModel.newMessage.observe(viewLifecycleOwner, {
            if (messagesArrayList.contains(it)) {
                val pos = messagesArrayList.indexOf(it)
                messagesArrayList[pos] = it
                val updatedList = channelMsgViewModel.getProfilePictures(
                    organizationID,
                    messagesArrayList
                )
                val channelsWithDateHeaders = createMessagesList(updatedList)
                channelListAdapter.submitList(channelsWithDateHeaders)
            } else {
                messagesArrayList.add(it)
                val updatedList = channelMsgViewModel.getProfilePictures(
                    organizationID,
                    messagesArrayList
                )
                val channelsWithDateHeaders = createMessagesList(updatedList)
                channelListAdapter.submitList(channelsWithDateHeaders)
                if (scrollDown) {
                    lifecycleScope.launch {
                        delay(100)
                        //binding.recyclerMessagesList.scrollToPosition(channelsWithDateHeaders.size-1)
                        binding.recyclerMessagesList.smoothScrollToPosition(channelsWithDateHeaders.size - 1)
                    }
                }
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

        sharedViewModel.newMessage.observe(viewLifecycleOwner, {

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
                        "https://api.zuri.chat/channels/${channel._id}"
                    )
                    intent.type = "text/plain"

                    val shareIntent = Intent.createChooser(intent, null)
                    startActivity(shareIntent)
                }
                R.id.channel_info -> {
                    val bundle = Bundle()
                    bundle.putString("channel_name", channel.name)
                    findNavController().navigate(R.id.channel_info_nav_graph, bundle)
                }
            }
            true
        }
    }

    val scrollDown = true
    private lateinit var job: Job
    private lateinit var uiScope: CoroutineScope

    private lateinit var client: Client
    private fun connectToSocket() {
        val channelChatEdit = binding.channelChatEditText

        uiScope.launch(Dispatchers.IO) {
            try {
                client = CentrifugeClient.getClient(requireActivity())
                client.connect()

                CentrifugeClient.setCustomListener(object : CentrifugeClient.CustomListener {
                    override fun onConnected(connected: Boolean) {
                        CentrifugeClient.subscribeToChannel(roomData!!.socket_name)
                    }

                    override fun onDataPublished(
                        subscription: Subscription?,
                        publishEvent: PublishEvent?
                    ) {
                        val dataString = String(publishEvent!!.data, StandardCharsets.UTF_8)
                        val data = Gson().fromJson(dataString, Data::class.java)
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
                channelChatEdit.text?.clear()

                /* val gson = Gson()
                 val dataString = gson.toJson(data).toString().toByteArray(Charsets.UTF_8)
                 sub!!.publish(dataString)*/
            }
        }
    }

    override fun onDestroy() {
        //connectHandler.disconnect(client)
        super.onDestroy()
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

    private fun generateID(): Int {
        return Random(6000000).nextInt()
    }

    private var messagesArrayList: ArrayList<Data> = ArrayList()
    private fun createMessagesList(channels: List<Data>): MutableList<BaseItem<*>> {
        // Wrap data in list items
        val channelsItems = channels.map {
            ChannelListItem(it, user, requireActivity(), uiScope)
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