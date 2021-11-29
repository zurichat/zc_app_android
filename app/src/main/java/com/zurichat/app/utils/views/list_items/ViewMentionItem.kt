package com.zurichat.app.utils.views.list_items

import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.zurichat.app.R
import com.zurichat.app.databinding.ItemChannelBinding
import com.zurichat.app.ui.base.BaseItem
import com.zurichat.app.utils.hide

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 23-Nov-21 at 4:12 PM
 *
 * Represents the view mention item in the channels list screen
 */
class ViewMentionItem :
    BaseItem<Unit, ItemChannelBinding>(Unit, R.layout.item_channel, R.layout.item_channel){

    override fun initializeViewBinding(view: View) = ItemChannelBinding.bind(view)
    override fun bind(binding: ItemChannelBinding) : Unit = with(binding){
        // remove the icon since it is not needed in the view mentions display
        imageIChannelPrivate.hide()
        textIChannelName.text = root.context.getString(R.string.view_mentions)
        // remove the margin before the text
        textIChannelName.updateLayoutParams<ViewGroup.MarginLayoutParams> { marginStart = 0 }
        // TODO: Remove this code when the backend exposes an api for getting mentions
        iChannelUnread.root.hide()
        // TODO: navigate to the mentions view
        // root.findNavController().navigate()
    }
}