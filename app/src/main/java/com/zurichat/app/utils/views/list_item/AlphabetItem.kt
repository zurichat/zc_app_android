package com.zurichat.app.utils.views.list_item

import android.graphics.Typeface
import android.widget.TextView
import androidx.core.view.setPadding
import com.zurichat.app.R
import com.zurichat.app.utils.views.list_item.binding.SingleViewBinding

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 29-Nov-21 at 5:55 PM
 *
 */
class AlphabetItem(alphabet: Char) : TextItem(alphabet.uppercaseChar().toString()){

    override fun bind(binding: SingleViewBinding) = with(binding.root as TextView){
        super.bind(binding)
        // change the font size and make the text bold
        textSize = resources.getDimension(R.dimen.alphabet_item_font_size)
        typeface = Typeface.DEFAULT_BOLD
        // set the padding on all sides of the item
        setPadding(resources.getDimension(R.dimen.alphabet_item_margin).toInt())
        // change the background color of the item
        setBackgroundResource(R.color.alphabet_item_background)
    }
}