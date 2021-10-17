package com.zurichat.app.ui.fragments.home_screen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentHomeScreenBinding
import com.zurichat.app.models.*
import com.zurichat.app.models.organization_model.OrgData
import com.zurichat.app.ui.activities.MainActivity
import com.zurichat.app.ui.dm.response.RoomListResponseItem
import com.zurichat.app.ui.dm_chat.model.response.room.RoomsListResponseItem
import com.zurichat.app.ui.dm_chat.repository.Repository
import com.zurichat.app.ui.dm_chat.utils.ModelPreferencesManager
import com.zurichat.app.ui.dm_chat.viewmodel.RoomViewModel
import com.zurichat.app.ui.dm_chat.viewmodel.RoomViewModelFactory
import com.zurichat.app.ui.fragments.home_screen.adapters.HomeFragmentPagerAdapter
import com.zurichat.app.ui.fragments.home_screen.chats_and_channels.localdatabase.AllChannelListObject
import com.zurichat.app.ui.fragments.switch_account.UserViewModel
import com.zurichat.app.ui.fragments.viewmodel.ChannelViewModel
import com.zurichat.app.ui.login.LoginViewModel
import com.zurichat.app.ui.newchannel.SelectNewChannelViewModel
import com.zurichat.app.ui.organizations.utils.ZuriSharePreference
import com.zurichat.app.util.ProgressLoader
import com.zurichat.app.util.Result
import com.zurichat.app.util.jsearch_view_utils.JSearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias Callback = () -> Unit

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    @Inject
    lateinit var progressLoader: ProgressLoader
    lateinit var binding: FragmentHomeScreenBinding
    private lateinit var user: User
    private var organization: OrgData? = null
    val viewModel: HomeScreenViewModel by viewModels()
    val userViewModel: LoginViewModel by viewModels()
    private val ViewModel by viewModels<UserViewModel>()
    private lateinit var organizationID: String
    private lateinit var organizationName: String
    private lateinit var memberId: String

    private lateinit var searchView: JSearchView
    private val roomViewModel: RoomViewModel by viewModels {
        val repository = Repository()
        RoomViewModelFactory(repository)
    }

    private val channelsViewModel: ChannelViewModel by viewModels()

    private lateinit var orgData: OrgData

    private val PREFS_NAME = "ORG_INFO"
    private val ORG_NAME = "org_name"
    private val ORG_ID = "org_id"
    private val MEM_ID = "mem_Id"
    private lateinit var sharedPref: SharedPreferences

    private val tabTitles = intArrayOf(R.string.chats, R.string.channels)
    private  val getOrgMembers: SelectNewChannelViewModel by viewModels()

    @Inject
    lateinit var preference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(searchView.onBackPressed()){
                    return
                }else{
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        user = requireActivity().intent.extras?.getParcelable("USER")!!
        val bundle = arguments
        organization = bundle?.getParcelable("Organization")
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        searchView = binding.toolbarContainer.searchView

        //get the label of the previous destination
        val prevDestLabel = findNavController().previousBackStackEntry?.destination?.label.toString()

        //Check if user just signed up and created organization or switched organization
        when (prevDestLabel) {
            "switch_organization", "fragment_see_your_channel" -> {
                //get the organization name passed from the previous destinations above
                organizationName = organization!!.name
                organizationID = organization!!.id
                memberId = organization!!.member_id
                //save the organization name and id to a sharedPreference to persist it
                with(sharedPref) {
                    edit().putString(ORG_NAME, organizationName).apply()
                    edit().putString(ORG_ID, organizationID).apply()
                    edit().putString(MEM_ID, memberId).apply()
                }
            }
            else -> {
                /**
                 * if the user is just logging in check if there is existing organization name
                 * saved in the sharedPreference and retrieve it or set the organization name to
                 * default if the sharedPreference does not contain it.
                 */
                if(sharedPref.contains(ORG_NAME)){
                    organizationName = sharedPref.getString(ORG_NAME, null).toString()
                }else{
                    organizationName = "Zuri Chat Default"
                }
                organizationID = sharedPref.getString(ORG_ID, null).toString()
                memberId = sharedPref.getString(MEM_ID, null).toString()
            }
        }
        //organizationID = "614679ee1a5607b13c00bcb7"
        if (ZuriSharePreference(requireContext()).getString("Current Organization ID","").isBlank()){
            val bundle = Bundle()
            bundle.putParcelable("USER", user)
            findNavController().navigate(R.id.switchOrganizationFragment, bundle)
        }
        try {
            getOrgMembers.orgID.value = organizationID
            getOrgMembers.getListOfUsers(organizationID)
        }catch (e : Exception){
            e.printStackTrace()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPagerAdapter = HomeFragmentPagerAdapter(childFragmentManager, lifecycle)
        val viewPager = binding.pager
        val tabs = binding.tabs
        val toolbar = binding.toolbarContainer.toolbar
        val activity = requireActivity() as MainActivity

        //set the toolbar title to the current logged in organization
        toolbar.subtitle = organizationName

        // setup for viewpager2 and tab layout
        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 2

        // attaches the viewpager to the tabs layout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(
                tabTitles[position]
            )
        }.attach()

        setupSearchView(toolbar.menu, tabs)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    val bundle = Bundle()
                    bundle.putParcelable("USER", user)
                    findNavController().navigate(R.id.settingsActivity, bundle)
                }
                R.id.search -> {

                }
                R.id.new_channel -> {
                    try {
                        findNavController().navigate(HomeScreenFragmentDirections.actionHomeScreenFragmentToNewChannelNavGraph())
                    } catch (exc: Exception) {
                        exc.printStackTrace()
                    }
                }
                R.id.switch_workspace -> {

                    val bundle = Bundle()
                    bundle.putParcelable("USER", user)
                    findNavController().navigate(R.id.switchOrganizationFragment, bundle)
                }
                R.id.invite_link -> {
                    findNavController().navigate(R.id.action_homeScreenFragment_to_shareLinkFragment)
                }
                R.id.switch_acc -> {
                 val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToAccountsFragment(user)
                 findNavController().navigate(action)
                }
            }
            true
        }

    }




    private fun setupSearchView(menu: Menu, tabLayout: TabLayout) = with(binding) {
        val searchResult = mutableListOf<SearchItem<RoomsListResponseItem, ChannelModel>>()

//        val channels

        val sv = toolbarContainer.searchView

        val item = menu.findItem(R.id.search)
        binding.toolbarContainer.searchView.setMenuItem(item)
        binding.toolbarContainer.searchView.setTabLayout(tabLayout)
        binding.toolbarContainer.searchView.setOnQueryTextListener(object : JSearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String): Boolean {

                if (tabLayout.selectedTabPosition == 0){
                    sv.handleResults(searchResult.filter {
                        if (it.room == null) return false
                        it.room.room_name.lowercase().contains(newText.lowercase())
                    })

                } else {
                    sv.handleResults(searchResult.filter {
                        if (it.channel == null) return false
                        it.channel.name.lowercase().contains(newText.lowercase())

                    })

                }

                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {

                if (tabLayout.selectedTabPosition == 0){
                    sv.handleResults(searchResult.filter {
                        it.room?.room_name?.lowercase()?.contains(query.lowercase())!!
                    })

                } else {
//                    sv.handleResults(searchResult.filter {
//                        it.channel?.name?.lowercase()?.contains(query.lowercase())!!
//                    })

                }
                return false
            }

            override fun onQueryTextCleared(): Boolean {
                searchResult.clear()
                return false
            }
        })

        sv.setClickListeners{ adapter->
            adapter.apply {
                setChannelItemClickListener {
                    val members = ArrayList<OrganizationMember>()
                    val bundle1 = Bundle()
                    bundle1.putParcelable("USER",user)
                    bundle1.putParcelable("Channel",it)
                    bundle1.putBoolean("Channel Joined",true)
                    bundle1.putParcelableArrayList("members", members as ArrayList<out Parcelable>)
                    sv.closeSearch()
                    findNavController().navigate(R.id.channelChatFragment,bundle1)

                }

                setRoomItemClickListener {
                    val bundle1 = Bundle()
                    bundle1.putParcelable("USER",user)
                    bundle1.putParcelable("room", it)
                    bundle1.putInt("position", 0)
                    sv.closeSearch()

                    findNavController().navigate(R.id.dmFragment, bundle1)
                }
            }
        }

    }

    private fun getChannels(channels: (List<ChannelModel>) -> Unit) {
        val channelsResponse = mutableListOf<ChannelModel>()

        channelsViewModel.getChannelsList(organizationID)
        channelsViewModel.channelsList.observe(viewLifecycleOwner) {
           it.forEach { channel->
               channelsResponse.add(channel)
           }
            channels(channelsResponse)
        }
    }

    private fun getRooms(rooms: (List<RoomsListResponseItem>)->Unit) {

        val roomsResponse = mutableListOf<RoomsListResponseItem>()
        roomViewModel.getRooms(organizationID, memberId)
        roomViewModel.myResponse.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                response.body()!!.forEach {
                    roomsResponse.add(it)
                }
                rooms(roomsResponse)
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
    }

}