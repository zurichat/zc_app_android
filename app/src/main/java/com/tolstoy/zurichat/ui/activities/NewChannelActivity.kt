package com.tolstoy.zurichat.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ActivityChannelNewBinding
import com.tolstoy.zurichat.models.NewChannel
import com.tolstoy.zurichat.ui.adapters.NewChannelAdapter

class NewChannelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityChannelNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this@NewChannelActivity,MainActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        }
        val newChannelAdapter = NewChannelAdapter(getNewChannel())
        binding.recyclerView.apply {
            adapter = newChannelAdapter
            layoutManager = LinearLayoutManager(this@NewChannelActivity)
        }
    }

    private fun getNewChannel(): List<NewChannel> {
        return listOf(
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_kolade_icon),
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_lux_icon),
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_mary_icon),
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_lux_icon),
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_mary_icon),
            NewChannel("Uche Klickworld", "God is great", R.drawable.ic_mary_icon),
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_mary_icon),
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_mary_icon),
            NewChannel("Olasubomi", "God is great", R.drawable.ic_lux_icon),
            NewChannel("Eniola Salami", "God is great", R.drawable.ic_lux_icon),
            NewChannel("Eniola Salami", "God is great", R.drawable.ic_lux_icon),
            NewChannel("Eniola Salami", "God is great", R.drawable.ic_lux_icon))
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
                return true
            }
            R.id.saved_messages -> {
                true
            }
            R.id.settings -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}