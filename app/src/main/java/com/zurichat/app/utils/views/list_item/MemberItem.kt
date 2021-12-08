package com.zurichat.app.utils.views.list_item

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.zurichat.app.R
import com.zurichat.app.databinding.ItemAttachmentImageBinding
import com.zurichat.app.ui.base.BaseItem
import com.zurichat.app.utils.dp

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 29-Nov-21 at 2:33 PM
 *
 * @param image the url of the image of the member
 */
class MemberItem(
    private val image: String
) : BaseItem<String, ItemAttachmentImageBinding>(
    image, R.layout.item_attachment_image, image
){
    override fun inflate(parent: ViewGroup) =
        ItemAttachmentImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun bind(binding: ItemAttachmentImageBinding) : Unit = with(binding){
        root.updateLayoutParams {
            width = 50.dp(root.resources)
        }
        Glide.with(root.context).load(image)
            .placeholder(R.drawable.ic_person)
            .into(imageIAI)
    }
}