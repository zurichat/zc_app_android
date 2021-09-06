package com.tolstoy.zurichat.ui.activities


import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityMainBinding
import com.tolstoy.zurichat.ui.adapters.HomeFragmentPagerAdapter
import com.tolstoy.zurichat.ui.adapters.RecyclerViewAdapter
import com.tolstoy.zurichat.ui.channel_info.ChannelInfoActivity
import com.tolstoy.zurichat.ui.fragment.ChatsFragment
import com.tolstoy.zurichat.ui.settings.SettingsActivity
import com.tolstoy.zurichat.util.setUpApplicationTheme

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private lateinit var mViewPager2: ViewPager2
    private var homeFragmentPagerAdapter: FragmentStateAdapter? = null
    private var rcAdapter: RecyclerViewAdapter? = null
    private lateinit var mTabLayout: TabLayout
    private var mTopToolbar: Toolbar? = null
    private val TAB_TITLES = intArrayOf(R.string.chats, R.string.channels)
    var chat = ChatsFragment()
    val searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)

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

        processSearch(item)

        return true
    }

    private fun processSearch(item: MenuItem?) {
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
                override fun onOptionsItemSelected(item: MenuItem): Boolean {
                    // Handle action bar item clicks here. The action bar will
                    // automatically handle clicks on the Home/Up button, so long
                    // as you specify a parent activity in AndroidManifest.xml.

                    when (item.itemId) {
                        R.id.search -> {
                            processSearch(item)
                        }
                        R.id.new_channel -> {
                            startActivity(Intent(this@MainActivity,NewChannelActivity::class.java))
                            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                            return false
                        }
                        R.id.saved_messages -> {
                            // Not implemented here
                            return false
                        }
                        R.id.settings -> {
                            intent = Intent(this, SettingsActivity::class.java)
                            startActivity(intent)
                            true
                        }

                        else -> {
                        }
                    }

                    return super.onOptionsItemSelected(item)

                }
            }
            private inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
                crossinline bindingInflater: (LayoutInflater) -> T
            ) =
                lazy(LazyThreadSafetyMode.NONE) {
                    bindingInflater.invoke(layoutInflater)
                }

