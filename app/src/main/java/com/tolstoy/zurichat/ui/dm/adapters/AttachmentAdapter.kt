package com.tolstoy.zurichat.ui.dm.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.tolstoy.zurichat.databinding.ItemAttachmentImageBinding
import com.tolstoy.zurichat.ui.dm.MEDIA

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 14-Sep-21
 */
class AttachmentAdapter(private val items: List<Uri>, private val media: MEDIA):
    RecyclerView.Adapter<AttachmentViewHolder>() {

    override fun getItemViewType(position: Int) = media.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            MEDIA.AUDIO.ordinal,
            MEDIA.VIDEO.ordinal,
            MEDIA.DOCUMENT.ordinal,
            MEDIA.IMAGE.ordinal -> AttachmentViewHolder.Image(ItemAttachmentImageBinding.inflate(inflater, parent, false))
            else -> AttachmentViewHolder.Image(ItemAttachmentImageBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size
}