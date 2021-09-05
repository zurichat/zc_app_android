package com.tolstoy.zurichat.ui.activities

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.channel_info.ChannelInfoActivity
import com.tolstoy.zurichat.ui.settings.SettingsActivity
import com.tolstoy.zurichat.util.setUpApplicationTheme


class ChannelChatActivity : AppCompatActivity() {
    private var mTopToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel_chat)

        mTopToolbar = findViewById(R.id.custom_toolbar)
        setSupportActionBar(mTopToolbar)
        // This setups application theme to value stored in sharedPref
        setUpApplicationTheme(this)

        //Launch Attachment Popup
        val attachment = findViewById<ImageView>(R.id.link_btn)
        val popupView: View = layoutInflater.inflate(R.layout.attachment_popup, null)
        var popupWindow = PopupWindow(
            popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT
        )
        popupWindow.setBackgroundDrawable(ColorDrawable())
        popupWindow.isOutsideTouchable = true

        attachment.setOnClickListener {
            popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 600)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.channel_chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.channel_info ->{
                intent = Intent(this, ChannelInfoActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}