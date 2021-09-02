package com.tolstoy.zurichat.ui.adapters

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.fragment.CallsFragment
import com.tolstoy.zurichat.ui.fragment.ChatsFragment


class HomeFragmentPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    val PAGE_COUNT = 2
    @StringRes
    val TAB_TITLES = intArrayOf(R.string.chats, R.string.calls)


    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = ChatsFragment()
            1 -> fragment = CallsFragment()
        }
        return ChatsFragment()
    }

    override fun getItemCount(): Int {
        return PAGE_COUNT
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
