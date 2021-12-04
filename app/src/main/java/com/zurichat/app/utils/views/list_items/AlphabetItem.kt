package com.zurichat.app.utils.views.list_items

import android.graphics.Typeface
import android.view.View
import androidx.core.view.setPadding
import com.zurichat.app.R
import com.zurichat.app.databinding.PartialHeaderBinding
import com.zurichat.app.ui.base.BaseItem

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 29-Nov-21 at 5:55 PM
 *
 */
class AlphabetItem(
    private val alphabet: Char
) : BaseItem<Char, PartialHeaderBinding>(alphabet, R.layout.partial_header, alphabet){

    override fun initializeViewBinding(view: View) = PartialHeaderBinding.bind(view)
    override fun bind(binding: PartialHeaderBinding) : Unit = with(binding){
        // make sure the displayed text is in uppercase
        textHeaderTitle.text = alphabet.uppercaseChar().toString()
        // change the font size and make the text bold
        textHeaderTitle.textSize = root.resources.getDimension(R.dimen.alphabet_item_font_size)
        textHeaderTitle.typeface = Typeface.DEFAULT_BOLD
        // set the padding on all sides of the item
        root.setPadding(root.resources.getDimension(R.dimen.alphabet_item_margin).toInt())
        // change the background color of the item
        root.setBackgroundResource(R.color.alphabet_item_background)
    }
}