package com.tolstoy.zurichat.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tolstoy.zurichat.ui.adapters.HomeFragmentPagerAdapter
import android.view.MenuItem

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.settings.SettingsActivity


class HomePageActivity : AppCompatActivity() {

    private lateinit var mViewPager2: ViewPager2
    private var homeFragmentPagerAdapter: FragmentStateAdapter? = null
    private lateinit var mTabLayout: TabLayout
    private val TAB_TITLES = intArrayOf(R.string.chats, R.string.calls)
//    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

//        mToolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(mToolbar)
        setSupportActionBar(findViewById(R.id.toolbar))


        //
        //Request permission for accessing media and files from storage
        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission())
            { isGranted: Boolean ->
                //Shows Toast message if permission is granted or denied.
                if (isGranted) {
                    Toast.makeText(this, "Permission Granted!", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show()
                }
            }

        //This runs automatically,
        //This checks if the permission has been granted, if it has, pass, else, it request for the permission using the function above
        //Comment this if and else statements to prevent permission from showing on startup.
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED ) {
            //Pass
        }
        else {//Request permission
            requestPermissionLauncher.launch(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

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
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search -> {
                true
            }
            R.id.new_channel -> {
                true
            }
            R.id.saved_messages -> {
                true
            }
            R.id.settings -> {
                intent = Intent(this, SettingsActivity::class.java)
                // start your next activity
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }


}