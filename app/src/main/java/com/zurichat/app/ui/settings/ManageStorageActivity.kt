package com.zurichat.app.ui.settings

import android.os.Bundle

import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.slider.Slider
import com.zurichat.app.R

import android.content.Intent


class ManageStorageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_storage)



        //references to the slider and progress bar
        val zuriMedia: Slider = findViewById(R.id.zuri_media_slider)
        val otherItems: Slider = findViewById(R.id.other_items_slider)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        //update storage value for zuri media
        zuriMedia.addOnChangeListener { slider, value, fromUser ->
            progressBar.progress = value.toInt()
        }
        //update storage value for other items
        otherItems.addOnChangeListener { slider, value, fromUser ->
            progressBar.secondaryProgress = value.toInt()
        }
        //to check for larger items in manage storage
        val midLayout: LinearLayout = findViewById(R.id.mid_layout)
        //set on click listener to mid layout
        midLayout.setOnClickListener{
            val intent = Intent(this,LargerItemsActivity::class.java)
            startActivity(intent)
        }
    }
}