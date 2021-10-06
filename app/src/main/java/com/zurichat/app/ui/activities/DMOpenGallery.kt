package com.zurichat.app.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.R
import com.zurichat.app.data.dmGallery.GalleryDataSource
import com.zurichat.app.ui.adapters.DmOpenGalleryAdapter

class DMOpenGallery : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dmopen_gallary)

        val myGallery = GalleryDataSource().loadGallery()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = DmOpenGalleryAdapter(this, myGallery)
        recyclerView.setHasFixedSize(true)
    }
}