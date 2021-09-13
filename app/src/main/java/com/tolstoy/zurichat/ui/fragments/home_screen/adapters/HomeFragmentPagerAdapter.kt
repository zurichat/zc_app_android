package com.tolstoy.zurichat.ui.fragments.home_screen.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels.ChannelsFragment
import com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels.ChatsFragment


class HomeFragmentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 ->  ChatsFragment()
            else -> ChannelsFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}

/*private const val NUM_TABS = 2

class HomeFragmentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return ChatsFragment()
            1 -> return CallsFragment()
        }
        return ChatsFragment()
    }
}*/
