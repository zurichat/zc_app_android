package com.zurichat.app.ui.settings.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zurichat.app.R

class ChooseWallpaperCategory: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_choose_wallpaper_category)
        title = "Choose Wallpaper Category"
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}