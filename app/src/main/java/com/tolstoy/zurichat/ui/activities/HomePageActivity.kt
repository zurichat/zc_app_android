package com.tolstoy.zurichat.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tolstoy.zurichat.ui.adapters.HomeFragmentPagerAdapter
import android.view.MenuItem

import android.widget.Toast
import com.tolstoy.zurichat.R


class HomePageActivity : AppCompatActivity() {

    private lateinit var mViewPager2: ViewPager2
    private var homeFragmentPagerAdapter: FragmentStateAdapter? = null
    private lateinit var mTabLayout: TabLayout
    private val TAB_TITLES = intArrayOf(R.string.chats, R.string.calls)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        mViewPager2 = findViewById(R.id.pager)
        homeFragmentPagerAdapter = HomeFragmentPagerAdapter(this)
        mViewPager2.adapter = homeFragmentPagerAdapter

        mTabLayout = findViewById(R.id.tabs)

        TabLayoutMediator(
            mTabLayout, mViewPager2
        ) { tab, position ->
            tab.text = resources.getString(
                TAB_TITLES[position]
            )
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }
}