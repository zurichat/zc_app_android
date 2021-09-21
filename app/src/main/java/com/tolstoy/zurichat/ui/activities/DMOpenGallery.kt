package com.tolstoy.zurichat.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.data.dmGallery.GalleryDataSource
import com.tolstoy.zurichat.models.DmOpenGalleryModel
import com.tolstoy.zurichat.ui.adapters.DmOpenGalleryAdapter

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