package com.zurichat.app.utils.views.list_item

import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import com.google.android.material.textview.MaterialTextView
import com.zurichat.app.ui.base.BaseItem
import com.zurichat.app.utils.views.list_item.BaseItemUtil.itemId
import com.zurichat.app.utils.views.list_item.binding.SingleViewBinding

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 23-Nov-21 at 4:12 PM
 *
 * Represents a text item
 *
 * @param text the text to display
 */
open class TextItem(
    private val text: String
) : BaseItem<String, SingleViewBinding>(text, itemId<TextItem>(), text){

    override fun inflate(parent: ViewGroup) = SingleViewBinding(
        MaterialTextView(parent.context).apply{
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        }
    )

    override fun bind(binding: SingleViewBinding) : Unit = with(binding.root as TextView){
        text = this@TextItem.text
    }
}