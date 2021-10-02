package com.tolstoy.zurichat.ui.fragments.home_screen

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.data.localSource.Cache
import com.tolstoy.zurichat.databinding.FragmentHomeScreenBinding
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.activities.MainActivity
import com.tolstoy.zurichat.ui.fragments.home_screen.adapters.HomeFragmentPagerAdapter
import com.tolstoy.zurichat.util.changeVisibility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeScreenFragment : Fragment(R.layout.fragment_home_screen) {

    lateinit var binding: FragmentHomeScreenBinding
    val viewModel: HomeScreenViewModel by activityViewModels()

    private val tabTitles = intArrayOf(R.string.chats, R.string.channels)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeScreenBinding.bind(view)
        setupUI()
        setupObservers()
    }

    private fun setupUI() = with(binding){
        // setup for viewpager2 and tab layout
        pager.also{
            it.adapter = HomeFragmentPagerAdapter(childFragmentManager, lifecycle)
            it.offscreenPageLimit = tabTitles.size
        }

        searchContainer.searchTextInputLayout.also {
            it.setStartIconOnClickListener {
                changeVisibility(View.GONE, searchContainer.root)
            }
            it.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.searchQuery.postValue(text.toString())
            }
        }

        toolbarContainer.toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.settings -> findNavController().navigate(R.id.settingsActivity)
                R.id.search -> searchContainer.also {
                    it.root.isVisible = true
                    it.searchTextInputLayout.editText?.requestFocus()
                }
                R.id.new_channel -> findNavController().navigate(R.id.action_homeScreenFragment_to_new_channel_nav_graph)
                R.id.saved_messages -> {
                }
                R.id.switch_workspace ->
                    findNavController().navigate(R.id.action_homeScreenFragment_to_switchOrganizationFragment)
            }
            true
        }

        // attaches the viewpager to the tabs layout
        TabLayoutMediator(tabs, pager) { tab, position ->
            tab.text = resources.getString(
                tabTitles[position]
            )
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupObservers() = with(viewModel){
        error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
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

    companion object{
        val TAG = HomeScreenFragment::class.simpleName
    }
}