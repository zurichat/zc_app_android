package com.zurichat.app.ui.main.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentHomeBinding
import com.zurichat.app.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 25-Oct-21 at 11:24 AM
 *
 */
@AndroidEntryPoint
class HomeFragment: BaseFragment(R.layout.fragment_home){

    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        setupUI()
    }

    private fun setupUI(): Unit = with(binding) {
        // setup the toolbar
        toolbarHome.root.apply{
            title = getString(R.string.zuri)
            logo = ResourcesCompat.getDrawable(resources, R.drawable.ic_app_icon, null)
            // add the menu and the menu click listener to the toolbar
            inflateMenu(R.menu.menu_home)
            setOnMenuItemClickListener { menuItem ->
                Toast.makeText(requireContext(), menuItem.title, Toast.LENGTH_SHORT).show()
                true
            }
        }
        // setup view pager
        pagerHome.apply{
            adapter = PagerAdapter(this@HomeFragment)
            offscreenPageLimit = TABS.size - 1
        }
        // attach the view pager to the tab layout
        TabLayoutMediator(tabsHome, pagerHome){ tab, position ->
            tab.text = resources.getString(TABS[position])
        }.attach()
    }

    private fun setupObservers(): Unit {}

    /**
     * The view pager adapter for the home screen
     *
     * @param fragment the fragment the view pager resides in
     * */
    class PagerAdapter(fragment: BaseFragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount() = TABS.size

        override fun createFragment(position: Int) = when (TABS[position]) {
            R.string.chats -> ChatListFragment()
            R.string.channels -> ChannelListFragment()
            else -> throw IllegalStateException("tab doesn't exist")
        }
    }

    companion object {
        /** Holds the titles for the tabs */
        private val TABS = intArrayOf(R.string.chats, R.string.channels)
    }
}