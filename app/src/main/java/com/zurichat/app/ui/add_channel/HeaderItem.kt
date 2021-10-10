package com.zurichat.app.ui.add_channel

import android.view.View
import com.zurichat.app.R
import com.zurichat.app.databinding.LayoutHeaderItemBinding

data class HeaderItem(val letter: String) : BaseItem<LayoutHeaderItemBinding> {

    override val layoutId = R.layout.layout_header_item

    override val uniqueId = letter

    override fun initializeViewBinding(view: View) = LayoutHeaderItemBinding.bind(view)

    override fun bind(binding: LayoutHeaderItemBinding, itemClickCallback: ((BaseItem<LayoutHeaderItemBinding>) -> Unit)?) {
        binding.textHeader.text = letter.uppercase()
    }
}