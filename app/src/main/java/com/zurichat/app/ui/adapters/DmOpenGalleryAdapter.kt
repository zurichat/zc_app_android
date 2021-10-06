package com.zurichat.app.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.R
import com.zurichat.app.models.DmOpenGalleryModel

class DmOpenGalleryAdapter( private val context: Context, private val gallery: List<DmOpenGalleryModel>):
    RecyclerView.Adapter<DmOpenGalleryAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val img = view.findViewById<ImageView>(R.id.img)
        val tv = view.findViewById<TextView>(R.id.galleryTv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.gallery_list_item, parent, false )
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
       val item = gallery[position]
        holder.img.setImageResource(item.ImageResource)
        holder.tv.text = context.resources.getString(item.text)
    }

    override fun getItemCount(): Int {
        return gallery.size
    }
}