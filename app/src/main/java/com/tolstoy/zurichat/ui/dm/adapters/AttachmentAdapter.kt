package com.tolstoy.zurichat.ui.dm.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.databinding.ItemAttachmentImageBinding
import com.tolstoy.zurichat.ui.dm.MEDIA

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 14-Sep-21
 */
class AttachmentAdapter(
    private val items: List<Uri>,
    private val media: MEDIA): RecyclerView.Adapter<AttachmentViewHolder>() {

    private val _selected = mutableListOf<Uri>()
    val selected: List<Uri> get() = _selected

    override fun getItemViewType(position: Int) = media.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val image = AttachmentViewHolder.Image(ItemAttachmentImageBinding.inflate(inflater, parent, false), _selected)
        return when(viewType){
            MEDIA.AUDIO.ordinal,
            MEDIA.VIDEO.ordinal,
            MEDIA.DOCUMENT.ordinal,
            MEDIA.IMAGE.ordinal -> image
            else -> image
        }
    }

    override fun onBindViewHolder(holder: AttachmentViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size
}