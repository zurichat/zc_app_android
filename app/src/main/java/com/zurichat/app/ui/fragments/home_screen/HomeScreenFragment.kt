package com.zurichat.app.ui.fragments.home_screen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentHomeScreenBinding
import com.zurichat.app.models.LogoutBody
import com.zurichat.app.models.User
import com.zurichat.app.ui.activities.MainActivity
import com.zurichat.app.ui.fragments.home_screen.adapters.HomeFragmentPagerAdapter
import com.zurichat.app.ui.fragments.switch_account.UserViewModel
import com.zurichat.app.ui.login.LoginViewModel
import com.zurichat.app.util.Result
import com.zurichat.app.util.jsearch_view_utils.JSearchView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    lateinit var binding: FragmentHomeScreenBinding
    private lateinit var user: User
    val viewModel: HomeScreenViewModel by viewModels()
    val userViewModel: LoginViewModel by viewModels()
    private val ViewModel by viewModels<UserViewModel>()
    private lateinit var organizationID: String
    private lateinit var organizationName: String

    private lateinit var searchView: JSearchView

    private val PREFS_NAME = "ORG_INFO"
    private val ORG_NAME = "org_name"
    private val ORG_ID = "org_id"
    private lateinit var sharedPref: SharedPreferences


    private val tabTitles = intArrayOf(R.string.chats, R.string.channels)

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        user = requireActivity().intent.extras?.getParcelable("USER")!!
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        searchView = binding.toolbarContainer.searchView

        //get the label of the previous destination
        val prevDestLabel = findNavController().previousBackStackEntry?.destination?.label.toString()

        //Check if user just signed up and created organization or switched organization
        when (prevDestLabel) {
            "switch_organization", "fragment_see_your_channel" -> {
                //get the organization name passed from the previous destinations above
                organizationName = arguments?.getString(ORG_NAME).toString()
                organizationID = arguments?.getString(ORG_ID).toString()
                //save the organization name and id to a sharedPreference to persist it
                with(sharedPref) {
                    edit().putString(ORG_NAME, organizationName).apply()
                    edit().putString(ORG_ID, organizationID).apply()
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
            }
        }
        //organizationID = "614679ee1a5607b13c00bcb7"
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
                R.id.starred_messages -> {
                    findNavController().navigate(R.id.action_homeScreenFragment_to_starredMessagesFragment)
                }
                R.id.switch_workspace -> {
                    val bundle = Bundle()
                    bundle.putParcelable("USER", user)
                    findNavController().navigate(R.id.switchOrganizationFragment, bundle)
                }
                R.id.invite_link -> {
                    findNavController().navigate(R.id.action_homeScreenFragment_to_shareLinkFragment)
                }
                R.id.logout -> {
                    logout()
                }
                R.id.switch_acc -> {
                 val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToAccountsFragment(user)
                 findNavController().navigate(action)
                }
            }
            true
        }
        observeData()
    }

    private fun observeData() {
        userViewModel.logoutResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Success -> {
                    Toast.makeText(context, "You have been successfully logged out", Toast.LENGTH_SHORT).show()
                    updateUser()
                    findNavController().navigate(R.id.action_homeScreenFragment_to_loginActivity)
                    requireActivity().finish()
                }
                is Result.Error -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun logout() {
        val logoutBody = LogoutBody(email = user.email)
        userViewModel.logout(logoutBody)
        userViewModel.clearUserAuthState()
    }
    private fun updateUser(){
        val user = user?.copy(currentUser = false)
        ViewModel.updateUser(user!!)
    }

    private fun setupSearchView(menu: Menu, tabLayout: TabLayout) = with(binding) {
        val item = menu.findItem(R.id.search)
        binding.toolbarContainer.searchView.setMenuItem(item)
        binding.toolbarContainer.searchView.setTabLayout(tabLayout)
        binding.toolbarContainer.searchView.setOnQueryTextListener(object : JSearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextCleared(): Boolean {
                return false
            }
        })

    }

}