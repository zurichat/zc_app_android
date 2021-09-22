package com.tolstoy.zurichat.ui.dm.adapters

import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.tolstoy.zurichat.databinding.ItemAttachmentAudioBinding
import com.tolstoy.zurichat.databinding.ItemAttachmentDocBinding
import com.tolstoy.zurichat.databinding.ItemAttachmentImageBinding
import com.tolstoy.zurichat.ui.dm.audio.AudioInfo
import com.tolstoy.zurichat.util.changeVisibility
/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 15-Sep-21
 */

sealed class AttachmentViewHolder(open val binding: ViewBinding, val selected: MutableList<Uri>):
    RecyclerView.ViewHolder(binding.root) {

    var isSelected = false
    abstract fun bind(uri: Uri)


    class Image(override val binding: ItemAttachmentImageBinding, selected: MutableList<Uri>) :
        AttachmentViewHolder(binding, selected) {

        override fun bind(uri: Uri) {
            binding.also {
                it.root.setOnClickListener { _ ->
                    if (!isSelected) {
                        // add the uri to the selected list
                        selected.add(uri)
                        changeVisibility(View.VISIBLE, it.attachmentSelected, it.attachmentTint)
                    } else {
                        selected.remove(uri)
                        changeVisibility(View.GONE, it.attachmentSelected, it.attachmentTint)
                    }
                    isSelected = !isSelected
                }
                Glide.with(it.root.context.applicationContext)
                    .load(uri).thumbnail(0.5f).into(binding.imageIAI)
            }
        }
    }

    class Audio(override val binding: ItemAttachmentAudioBinding, selected: MutableList<Uri>) :
        AttachmentViewHolder(binding, selected) {

        override fun bind(uri: Uri) {
            binding.also {
                it.root.setOnClickListener { _ ->
                    if (!isSelected) {
                        // add the uri to the selected list
                        selected.add(uri)
                        changeVisibility(View.VISIBLE, it.view4, it.tick)
                    } else {
                        selected.remove(uri)
                        changeVisibility(View.GONE, it.view4, it.tick)
                    }
                    isSelected = !isSelected
                }
                it.root.context.applicationContext.let {

                }
            }
        }
    }

    class Document(override val binding: ItemAttachmentDocBinding, selected: MutableList<Uri>) :
        AttachmentViewHolder(binding, selected) {

        override fun bind(uri: Uri) {
            binding.also {
                it.root.setOnClickListener { _ ->
                    if (!isSelected) {
                        // add the uri to the selected list
                        selected.add(uri)
                        changeVisibility(View.VISIBLE, it.selectColor)
                    } else {
                        selected.remove(uri)
                        changeVisibility(View.GONE, it.selectColor)
                    }
                    isSelected = !isSelected
                }
            }
        }
    }

}