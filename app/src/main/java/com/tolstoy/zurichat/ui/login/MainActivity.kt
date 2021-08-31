package com.tolstoy.zurichat.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.adapter.PageAdapter
import com.tolstoy.zurichat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var toolbar: androidx.appcompat.widget.Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // Call drawerLayout function
        drawerLayoutFunction()
    }

    // drawer layout function
    private fun drawerLayoutFunction() {

        val drawerLayout = binding.drawerLayoutID
        val navView = binding.navView
        toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {

            when (it.itemId) {

                R.id.view_profile -> Toast.makeText(this, "Open View Profile", Toast.LENGTH_LONG)
                    .show()
                R.id.presence -> Toast.makeText(this, "Open Presence", Toast.LENGTH_LONG).show()
                R.id.channels -> Toast.makeText(this, "Open Channels", Toast.LENGTH_LONG).show()
                R.id.notification -> Toast.makeText(this, "Open Notification", Toast.LENGTH_LONG)
                    .show()
                R.id.settings -> Toast.makeText(this, "Open Settings", Toast.LENGTH_LONG).show()
                R.id.logout -> Toast.makeText(this, "Open Logout", Toast.LENGTH_LONG).show()
                R.id.out_icon -> Toast.makeText(this, "Open Out", Toast.LENGTH_LONG).show()
            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
        crossinline bindingInflater: (LayoutInflater) -> T
    ) =
        lazy(LazyThreadSafetyMode.NONE) {
            bindingInflater.invoke(layoutInflater)
        }
}