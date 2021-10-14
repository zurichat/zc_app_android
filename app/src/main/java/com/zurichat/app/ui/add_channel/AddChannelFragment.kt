package com.zurichat.app.ui.add_channel

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.zurichat.app.R
import com.zurichat.app.data.localSource.AppDatabase
import com.zurichat.app.data.localSource.dao.OrganizationMembersDao
import com.zurichat.app.databinding.FragmentAddChannelBinding
import com.zurichat.app.models.ChannelModel
import com.zurichat.app.models.OrganizationMember
import com.zurichat.app.models.User
import com.zurichat.app.util.mapToMemberList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AddChannelFragment : Fragment() {
    private lateinit var binding: FragmentAddChannelBinding
    private lateinit var channelListAdapter : BaseListAdapter

    private var user : User? = null
    private lateinit var channelsArrayList: ArrayList<ChannelModel>
    private lateinit var joinedChannelsArrayList: ArrayList<ChannelModel>

    var userList: List<OrganizationMember> = ArrayList()
    lateinit var sharedPref: SharedPreferences
    private val PREFS_NAME = "ORG_INFO"
    private val ORG_NAME = "org_name"
    private val ORG_ID = "org_id"

    private lateinit var job: Job
    private lateinit var uiScope: CoroutineScope

    private lateinit var database: AppDatabase
    private lateinit var organizationMembersDao: OrganizationMembersDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAddChannelBinding.inflate(inflater, container, false)
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val bundle = arguments
        if (bundle != null) {
            user = bundle.getParcelable("USER")
            channelsArrayList = bundle.getParcelableArrayList("Channels List")!!
            joinedChannelsArrayList = bundle.getParcelableArrayList("Joined Channels List")!!

            binding.channelToolbar.subtitle = channelsArrayList.size.toString().plus(" channels")

            val channelsWithAlphabetHeaders = createAlphabetizedChannelsList(channelsArrayList)

            channelListAdapter = BaseListAdapter { channelItem ->
                val bundle1 = Bundle()
                bundle1.putParcelable("USER",user)
                bundle1.putParcelable("Channel",(channelItem as ListItem).channel)
                if (joinedChannelsArrayList.contains(channelItem.channel)){
                    bundle1.putBoolean("Channel Joined",true)
                }
                findNavController().navigate(R.id.channelChatFragment,bundle1)
            }

            binding.channelsList.adapter = channelListAdapter
            binding.searchListEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    val filteredChannelsWithAlphabetHeaders = filterAlphabetizedChannelsList(channelsArrayList,s.toString().trim())
                    channelListAdapter.submitList(filteredChannelsWithAlphabetHeaders)
                }

            })
            channelListAdapter.submitList(channelsWithAlphabetHeaders)
        }

        binding.channelToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        job = Job()
        uiScope = CoroutineScope(Dispatchers.Main + job)

        database = Room.databaseBuilder(requireActivity().applicationContext, AppDatabase::class.java, "zuri_chat").build()
        organizationMembersDao = database.organizationMembersDao()

        val listString = sharedPref.getString("User List","")
        var organizationID = sharedPref.getString(ORG_ID,"")
        //This Will Be Removed Later.
        organizationID = "6145eee9285e4a18402074cd"
        if (!(organizationID.isNullOrBlank())){
            uiScope.launch(Dispatchers.IO) {
                organizationMembersDao.getMembers(organizationID).collect {
                    try{
                        userList = it.mapToMemberList()
                        uiScope.launch(Dispatchers.Main) {
                            binding.newChannel.isEnabled = true
                        }
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }
            }
        }
        /*val gson = Gson()
        if (listString != null) {
            if (listString.isNotBlank()){
                userList = gson.fromJson(listString,Array<OrganizationMember>::class.java).toList()
            }
        }*/
        binding.newChannel.setOnClickListener {
            try {
                findNavController().navigate(R.id.selectMember, bundleOf(
                    Pair("USER_LIST",userList),
                    Pair("AddChannelFragment",true)
                ))
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }
        return binding.root
    }

    private fun createAlphabetizedChannelsList(channels: List<ChannelModel>): MutableList<BaseItem<*>> {
        // Wrap data in list items
        val channelsItems = channels.map {
            ListItem(it,requireActivity(),joinedChannelsArrayList)
        }.sortedBy {
            it.channel.name.lowercase()
        }

        val channelsWithAlphabetHeaders = mutableListOf<BaseItem<*>>()

        // Loop through the channels list and add headers where we need them
        var currentHeader: String? = null
        channelsItems.forEach{ c->
            c.channel.name.firstOrNull()?.toString()?.let {
                if (it != currentHeader){
                    channelsWithAlphabetHeaders.add(HeaderItem(it))
                    currentHeader = it
                }
            }
            channelsWithAlphabetHeaders.add(c)
        }
        return channelsWithAlphabetHeaders
    }

    private fun filterAlphabetizedChannelsList(channels: List<ChannelModel>, search: String): MutableList<BaseItem<*>> {
        // Wrap data in list items
        val channelsItems = channels.map {
            ListItem(it,requireActivity(),joinedChannelsArrayList)
        }.sortedBy {
            it.channel.name.lowercase()
        }

        val channelsWithAlphabetHeaders = mutableListOf<BaseItem<*>>()

        // Loop through the channels list and add headers where we need them
        var currentHeader: String? = null
        channelsItems.forEach{ c->
            if(c.channel.name.lowercase().contains(search.lowercase())){
                c.channel.name.firstOrNull()?.toString()?.let {
                    if (it != currentHeader){
                        channelsWithAlphabetHeaders.add(HeaderItem(it))
                        currentHeader = it
                    }
                }
                channelsWithAlphabetHeaders.add(c)
            }
        }
        return channelsWithAlphabetHeaders
    }

}