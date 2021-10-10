package com.zurichat.app.ui.fragments.home_screen.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zurichat.app.ui.fragments.home_screen.chats_and_channels.ChannelsFragment
import com.zurichat.app.ui.fragments.home_screen.chats_and_channels.ChatsFragment


class HomeFragmentPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val fragmentMutableLiveData =MutableLiveData<Fragment>()
    val fragmentLiveData get() = fragmentMutableLiveData


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                fragmentMutableLiveData.value = ChatsFragment()
                ChatsFragment()
            }
            else -> {
                fragmentMutableLiveData.value = ChannelsFragment()
                ChannelsFragment()
            }
        }
    }



    override fun getItemCount(): Int {
        return 2
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)

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
