package com.zurichat.app.utils.views.list_item

import android.R
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.zurichat.app.ui.base.BaseItem
import com.zurichat.app.utils.dp
import com.zurichat.app.utils.drawableFromAttr
import com.zurichat.app.utils.views.list_item.BaseItemUtil.itemId
import com.zurichat.app.utils.views.list_item.binding.SingleViewBinding

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 28-Nov-21 at 1:48 PM
 *
 */
class DividerItem: BaseItem<Unit, SingleViewBinding>(Unit, itemId<DividerItem>(), itemId<DividerItem>()) {

    override fun inflate(parent: ViewGroup) = SingleViewBinding (
        View(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 2.dp(resources))
            background = context.drawableFromAttr(R.attr.listDivider)
        }
    )

    override fun bind(binding: SingleViewBinding) {}
}