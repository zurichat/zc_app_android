package com.tolstoy.zurichat.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.google.android.material.slider.Slider
import com.tolstoy.zurichat.R

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
    }
}