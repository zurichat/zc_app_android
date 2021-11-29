package com.zurichat.app.utils.views.list_items

import android.view.View
import com.zurichat.app.R
import com.zurichat.app.databinding.PartialDividerBinding
import com.zurichat.app.ui.base.BaseItem

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 28-Nov-21 at 1:48 PM
 *
 */
class DividerItem:
    BaseItem<Unit, PartialDividerBinding>(Unit, R.layout.partial_divider, R.id.divider,) {

    override fun initializeViewBinding(view: View) = PartialDividerBinding.bind(view)
    override fun bind(binding: PartialDividerBinding) {}
}