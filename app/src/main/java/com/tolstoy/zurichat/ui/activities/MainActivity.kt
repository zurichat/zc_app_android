package com.tolstoy.zurichat.ui.activities


import android.graphics.Color
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityMainBinding
import com.tolstoy.zurichat.ui.adapters.HomeFragmentPagerAdapter
import android.text.style.ForegroundColorSpan

import android.text.SpannableString

import com.tolstoy.zurichat.util.setUpApplicationTheme


class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private lateinit var mViewPager2: ViewPager2
    private var homeFragmentPagerAdapter: FragmentStateAdapter? = null
    private lateinit var mTabLayout: TabLayout
    private var mTopToolbar: Toolbar? = null
    private val TAB_TITLES = intArrayOf(R.string.chats, R.string.calls)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        mTopToolbar = findViewById(R.id.my_toolbar)
        setSupportActionBar(mTopToolbar)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_menu, menu)
        val menuItem: MenuItem = menu!!.findItem(R.id.search)
        val searchView: SearchView = menuItem.actionView as SearchView
        searchView.queryHint = "Search"
        val positionOfMenuItem = 0 // or whatever...

        val item = menu.getItem(positionOfMenuItem)
        val s = SpannableString("My red MenuItem")
        s.setSpan(ForegroundColorSpan(Color.WHITE), 0, s.length, 0)
        item.title = s

        searchView.setOnSearchClickListener {object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        when (item.itemId) {
            R.id.search ->
                return true
            R.id.new_channel ->
                // Do Activity menu item stuff here
                return false
            R.id.saved_messages->
                // Not implemented here
                return false
            R.id.settings->
                // Not implemented here
                return false
            else -> {
            }
        }

        return super.onOptionsItemSelected(item)

        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)
    }

    private inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
        crossinline bindingInflater: (LayoutInflater) -> T
    ) =
        lazy(LazyThreadSafetyMode.NONE) {
            bindingInflater.invoke(layoutInflater)}
}