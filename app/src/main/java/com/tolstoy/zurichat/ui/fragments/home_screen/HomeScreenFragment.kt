package com.tolstoy.zurichat.ui.fragments.home_screen

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentHomeScreenBinding
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.activities.MainActivity
import com.tolstoy.zurichat.ui.fragments.home_screen.adapters.HomeFragmentPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeScreenFragment : Fragment() {
    private lateinit var binding: FragmentHomeScreenBinding
    private lateinit var user: User
    val viewModel: HomeScreenViewModel by viewModels()
    private lateinit var organizationID: String
    private lateinit var organizationName: String

    private val PREFS_NAME = "ORG_INFO"
    private val ORG_NAME = "org_name"
    private val ORG_ID = "org_id"
    private lateinit var sharedPref: SharedPreferences

    private val tabTitles = intArrayOf(R.string.chats, R.string.channels)

    @Inject
    lateinit var preference: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false)

        user = requireActivity().intent.extras?.getParcelable("USER")!!
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
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

        //get the label of the previous destination
        val prevDestLabel = findNavController().previousBackStackEntry
            ?.destination?.label.toString()

        when (prevDestLabel) {
            "switch_organization", "fragment_see_your_channel" -> {
                organizationName = arguments?.getString(ORG_NAME).toString()
                organizationID = arguments?.getString(ORG_ID).toString()
                with(sharedPref) {
                    edit().putString(ORG_NAME, organizationName).apply()
                    edit().putString(ORG_ID, organizationID).apply()
                }
            }
            else -> {
                organizationName = sharedPref.getString(ORG_NAME, null).toString()
                organizationID = sharedPref.getString(ORG_ID, null).toString()
                when {
                    organizationName.equals(null) -> {
                        organizationName = "Zuri Chat Default"
                        organizationID = "614679ee1a5607b13c00bcb7"
                    }
                }
            }
        }

        //set the toolbar title to the current logged in organization
        toolbar.title = organizationName

        // setup for viewpager2 and tab layout
        viewPager.adapter = viewPagerAdapter
        viewPager.offscreenPageLimit = 2

        // attaches the viewpager to the tabs layout
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(
                tabTitles[position]
            )
        }.attach()

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.settings -> {
                    val bundle = Bundle()
                    bundle.putParcelable("USER", user)
                    findNavController().navigate(R.id.settingsActivity, bundle)
                }
                R.id.search -> {
                    binding.searchContainer.root.isVisible = true
                    binding.searchContainer.searchTextInputLayout.editText?.requestFocus()
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
                    val bundle = bundleOf("email" to user.email)
                    findNavController().navigate(R.id.switchOrganizationFragment, bundle)
                }
                R.id.invite_link -> {
                    findNavController().navigate(R.id.action_homeScreenFragment_to_shareLinkFragment)
                }
            }
            true
        }
        binding.searchContainer.searchTextInputLayout.setStartIconOnClickListener {
            binding.searchContainer.root.isVisible = false
        }

        binding.searchContainer.searchTextInputLayout.editText?.doOnTextChanged { text, start, before, count ->
            viewModel.searchQuery.postValue(text.toString())
        }
    }

    /**private fun processSearch(item: MenuItem?) {
    val s = SpannableString("My MenuItem")
    s.setSpan(ForegroundColorSpan(Color.WHITE), 0, s.length, 0)
    if (item != null) {
    item.title = s
    }
    searchView?.setOnSearchClickListener {
    object : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?): Boolean {
    var str = rcAdapter?.filter(query.toString())

    if (str == null) {
    Toast.makeText(
    this@MainActivity,
    "No Match found",
    Toast.LENGTH_LONG
    ).show()
    }
    return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
    rcAdapter?.filter(newText.toString())
    return true
    }
    }
    }
    }
     */

}