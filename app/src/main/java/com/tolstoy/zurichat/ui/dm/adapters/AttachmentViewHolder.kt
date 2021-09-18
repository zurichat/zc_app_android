package com.tolstoy.zurichat.ui.dm.adapters

import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.tolstoy.zurichat.databinding.ItemAttachmentImageBinding

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 15-Sep-21
 */
sealed class AttachmentViewHolder(open val binding: ViewBinding):
    RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(uri: Uri)

    class Image(override val binding: ItemAttachmentImageBinding): AttachmentViewHolder(binding){

        override fun bind(uri: Uri) {
            Glide.with(binding.root.context.applicationContext)
                .load(uri).thumbnail(0.5f).into(binding.imageIAI)
        }
    }
}