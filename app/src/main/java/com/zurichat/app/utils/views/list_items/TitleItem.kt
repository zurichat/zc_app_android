package com.zurichat.app.utils.views.list_items

import android.view.View
import com.zurichat.app.R
import com.zurichat.app.databinding.PartialHeaderBinding
import com.zurichat.app.ui.base.BaseItem
import com.zurichat.app.utils.show

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 23-Nov-21 at 4:12 PM
 *
 * Represents a text with a circular button aligned to the far right of it
 *
 * @param title the title to display
 * @param showButton should the button be shown
 * @param buttonClick the callback that is invoked when the button is clicked
 */
class TitleItem(
    private val title: String,
    private val showButton: Boolean = false,
    private val buttonClick: (() -> Unit)? = null
) : BaseItem<String, PartialHeaderBinding>(title, R.layout.partial_header, title){

    override fun initializeViewBinding(view: View) = PartialHeaderBinding.bind(view)
    override fun bind(binding: PartialHeaderBinding) : Unit = with(binding){
        textHeaderTitle.text = title
        if(showButton) imageHeaderAdd.apply{
            show()
            setOnClickListener {
                buttonClick?.invoke()
            }
        }
    }
}