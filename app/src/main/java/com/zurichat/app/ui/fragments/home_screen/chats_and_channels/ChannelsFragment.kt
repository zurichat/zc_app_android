package com.zurichat.app.ui.fragments.home_screen.chats_and_channels

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Room
import centrifuge.Client
import com.google.android.material.snackbar.Snackbar
import com.zurichat.app.R
import com.zurichat.app.data.localSource.AppDatabase
import com.zurichat.app.databinding.FragmentChannelsBinding
import com.zurichat.app.models.ChannelModel
import com.zurichat.app.models.User
import com.zurichat.app.ui.fragments.channel_chat.localdatabase.RoomDao
import com.zurichat.app.ui.fragments.home_screen.CentrifugeClient
import com.zurichat.app.ui.fragments.home_screen.HomeScreenFragment
import com.zurichat.app.ui.fragments.home_screen.adapters.ChannelAdapter
import com.zurichat.app.ui.fragments.home_screen.chats_and_channels.localdatabase.AllChannelListDao
import com.zurichat.app.ui.fragments.home_screen.chats_and_channels.localdatabase.AllChannelListObject
import com.zurichat.app.ui.fragments.home_screen.chats_and_channels.localdatabase.ChannelListDao
import com.zurichat.app.ui.fragments.home_screen.chats_and_channels.localdatabase.ChannelListObject
import com.zurichat.app.ui.fragments.home_screen.diff_utils.ChannelDiffUtil
import com.zurichat.app.ui.fragments.viewmodel.ChannelViewModel
import com.zurichat.app.ui.fragments.viewmodel.SharedChannelViewModel
import com.zurichat.app.ui.notification.NotificationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class ChannelsFragment : Fragment(R.layout.fragment_channels) {
    private val viewModel : ChannelViewModel by viewModels()
    private lateinit var sharedViewModel : SharedChannelViewModel
    private val PREFS_NAME = "ORG_INFO"
    private val ORG_ID = "org_id"
    private lateinit var binding: FragmentChannelsBinding
    private lateinit var channelsArrayList: ArrayList<ChannelModel>
    private lateinit var joinedArrayList: ArrayList<ChannelModel>
    private lateinit var originalChannelsArrayList: ArrayList<ChannelModel>
    private lateinit var user : User
    private lateinit var organizationID: String

    private lateinit var job:Job
    private lateinit var uiScope: CoroutineScope
    private lateinit var client: Client

    private lateinit var database: AppDatabase
    private lateinit var roomDao: RoomDao
    private lateinit var channelListDao: ChannelListDao
    private lateinit var allChannelListDao: AllChannelListDao
    private lateinit var sharedPref: SharedPreferences

    private val mNotificationTime = Calendar.getInstance().timeInMillis + 5000 //Set after 5 seconds from the current time.
    private var mNotified = false

    private val newChannelMenu by lazy {
        (parentFragment as HomeScreenFragment).binding.toolbarContainer.toolbar.menu.findItem(R.id.new_channel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChannelsBinding.inflate(inflater, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedChannelViewModel::class.java)

        database = Room.databaseBuilder(requireActivity().applicationContext, AppDatabase::class.java, "zuri_chat").build()
        roomDao = database.roomDao()
        channelListDao = database.channelListDao()
        allChannelListDao = database.allChannelListDao()

        user = requireActivity().intent.extras?.getParcelable("USER")!!
        //organizationID = "614679ee1a5607b13c00bcb7"
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        organizationID = sharedPref.getString(ORG_ID, "614679ee1a5607b13c00bcb7").toString()

        job = Job()
        uiScope = CoroutineScope(Dispatchers.Main + job)

        if (!mNotified) {
            NotificationUtils().setNotification(mNotificationTime, requireActivity())
        }

        return binding.root
    }

    private lateinit var adapt:ChannelAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        channelsArrayList = ArrayList()
        joinedArrayList = ArrayList()
        originalChannelsArrayList = ArrayList()
        //addHeaders()
        getListOfChannels()

        view.findViewById<LinearLayoutCompat>(R.id.mentionLayout_).setOnClickListener {
            if (channelsArrayList.isNotEmpty()){
                val bundle = Bundle()
                bundle.putParcelable("USER",user)
                findNavController().navigate(R.id.mentionFragment,bundle)
            }
        }
    }

    private fun generateRandomLong(): Long {
        val rand = Random(10000)
        return rand.nextLong()
    }

    override fun onResume() {
        super.onResume()
        newChannelMenu.isVisible = true
    }

    override fun onPause() {
        super.onPause()
        newChannelMenu.isVisible = false
    }

    /***
     * Headers Are Added Here. This will also be called after every update on the list to properly update the header positions
     */
    private fun addHeaders(){
        uiScope.launch(Dispatchers.IO){
            try{
                client = CentrifugeClient.getClient(requireActivity())
                client.connect()
            }catch (e : Exception){
                e.printStackTrace()
            }
        }

        val newList: ArrayList<ChannelModel> = ArrayList()

        val unreadList: ArrayList<ChannelModel> = ArrayList()
        val unreadChannelHeader = ChannelModel(getString(R.string.unread_messages), false, false, "channel_header_unread", generateRandomLong().toString(), 0)

        val readList: ArrayList<ChannelModel> = ArrayList()
        val addChannelHeader = ChannelModel(getString(R.string._add_channel), false, false, "channel_header_add", generateRandomLong().toString(), 0)
        val dividerHeader = ChannelModel("", false, false, "channel_header_add", generateRandomLong().toString(), 2)

        //display fab if channel list is empty
        val fabButton = binding.fabAddChannel

        for (channel in channelsArrayList){
            if (channel.isRead){
                readList.add(channel)
            }else{
                unreadList.add(channel)
            }
        }

        if (unreadList.size>0){
            newList.add(unreadChannelHeader)
            for (channel in unreadList){
                //This makes sure there are no duplicate headers
                if (!(channel.type == "channel_header_unread" || channel.type == "channel_header_add")){
                    newList.add(channel)
                }
            }

            // Makes sure addition of divider is not repeated
            if (!newList.contains(dividerHeader)){
                newList.add(dividerHeader)
            }
        }
        newList.add(addChannelHeader)
        for (channel in readList){
            //This makes sure there are no duplicate headers
            if (!(channel.type == "channel_header_unread" || channel.type == "channel_header_add")){
                newList.add(channel)
            }
        }

        val diffResult = DiffUtil.calculateDiff(ChannelDiffUtil(channelsArrayList, newList))
        channelsArrayList.clear()
        channelsArrayList.addAll(newList)

        /**
         * Sets up adapter after channelList has been computed
         */
        adapt = ChannelAdapter(requireActivity(), channelsArrayList,uiScope, roomDao)
        adapt.organizationId = organizationID
        adapt.setItemClickListener {
            val bundle1 = Bundle()
            bundle1.putParcelable("USER",user)
            bundle1.putParcelable("Channel",it)
            bundle1.putBoolean("Channel Joined",true)
            findNavController().navigate(R.id.channelChatFragment,bundle1)
        }
        adapt.setAddChannelClickListener {
            val bundle = Bundle()
            bundle.putParcelable("USER",user)
            bundle.putParcelableArrayList("Channels List",originalChannelsArrayList)
            bundle.putParcelableArrayList("Joined Channels List",channelsArrayList)
            findNavController().navigate(R.id.addChannelFragment,bundle)
        }
        binding.channelRecycleView.adapter = adapt
        diffResult.dispatchUpdatesTo(adapt)

        if(channelsArrayList.isEmpty()){
            fabButton.visibility = View.VISIBLE
            fabButton.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("USER",user)
                bundle.putParcelableArrayList("Channels List",originalChannelsArrayList)
                bundle.putParcelableArrayList("Joined Channels List",channelsArrayList)
                findNavController().navigate(R.id.addChannelFragment,bundle)
            }
        }else{
            fabButton.visibility = View.GONE
        }
    }

    private fun getListOfChannels() {
        uiScope.launch(Dispatchers.IO){
            allChannelListDao.getChannelListWithOrganizationID(organizationID).let {
                uiScope.launch(Dispatchers.Main){
                    if (it!=null){
                        originalChannelsArrayList.clear()
                        originalChannelsArrayList.addAll(it.channelList)
                    }
                }
            }

            channelListDao.getChannelListWithOrganizationID(organizationID).let {
                uiScope.launch(Dispatchers.Main){
                    if (it!=null){
                        channelsArrayList.clear()
                        channelsArrayList.addAll(it.channelList)
                        addHeaders()
                    }
                }
            }
        }

        viewModel.isConnected(false)
        viewModel.getChannelsList(organizationID)
        viewModel.channelsList.observe(viewLifecycleOwner,{
            originalChannelsArrayList.clear()
            originalChannelsArrayList.addAll(it)

            uiScope.launch(Dispatchers.IO){
                val allChannelListObject = AllChannelListObject(it)
                allChannelListObject.orgId = organizationID
                allChannelListDao.insertAll(allChannelListObject)
            }
            viewModel.getJoinedChannelsList(organizationID,user.id)
        })

        viewModel.joinedChannelsList.observe(viewLifecycleOwner,{ joinedList ->
            channelsArrayList.clear()
            val arrayList = ArrayList<ChannelModel>()
            if (joinedList.isNotEmpty()){
                joinedList.forEach{ joinedChannel ->
                    originalChannelsArrayList.forEach{ channel ->
                        if (joinedChannel.id == channel._id){
                            channelsArrayList.add(channel)
                            arrayList.add(channel)
                        }
                    }
                }
            }
            uiScope.launch(Dispatchers.IO){
                val channelListObject = ChannelListObject(arrayList)
                channelListObject.orgId = organizationID
                channelListDao.insertAll(channelListObject)
            }
            addHeaders()
        })

        viewModel.error.observe(viewLifecycleOwner,{
            if (it!=null){
                if (channelsArrayList.isEmpty()){
                    //Show Snack bar
                    showSnackBar()
                }else {
                    //Recycle View isn't empty but calls to get the channels list fails. Log the error without compromising user experience
                    Timber.i(it)
                }
            }
        })

        viewModel.newMessage.observe(viewLifecycleOwner,{
            for (channel in channelsArrayList){
                if (channel._id == it.channel_id){
                    channel.isRead = false
                }
            }
        })
    }

    private fun showSnackBar() {
        val view: View = CoordinatorLayout(requireContext())
        val snack = Snackbar.make(view, "An Error Occurred!", Snackbar.LENGTH_INDEFINITE)
        snack.setAction("Retry") {
            viewModel.getChannelsList(organizationID)
        }
        snack.show()
    }

}
