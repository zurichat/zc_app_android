package com.tolstoy.zurichat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tolstoy.zurichat.fragment.ChannelFragment
import com.tolstoy.zurichat.fragment.ChatFragment

class PageAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> (return ChatFragment())
            1 -> (return ChannelFragment())
            else -> (return ChatFragment())
        }
    }

}