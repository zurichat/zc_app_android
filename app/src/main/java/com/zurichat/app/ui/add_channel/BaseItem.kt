package com.zurichat.app.ui.add_channel

import android.view.View
import androidx.viewbinding.ViewBinding

interface BaseItem<T : ViewBinding> {

    val layoutId: Int

    // Used to compare items when diffing so RecyclerView knows how to animate
    val uniqueId: Any

    fun initializeViewBinding(view: View): T

    /**
     * @param itemClickCallback Optional click callback for clicks on the whole item
     * */
    fun bind(holder: BaseViewHolder<*>, itemClickCallback: ((BaseItem<T>) -> Unit)?) {
        val specificHolder = holder as BaseViewHolder<T>
        bind(specificHolder.binding, itemClickCallback)
    }

    fun bind(binding: T, itemClickCallback: ((BaseItem<T>) -> Unit)?)

    // Make sure implementations implement equals function (data classes do already)
    override fun equals(other: Any?): Boolean
}