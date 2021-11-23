package com.zurichat.app.ui.base

import android.view.View
import androidx.viewbinding.ViewBinding

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 23-Nov-21 at 10:55 AM
 *
 * The base item to be used in recycler views especially when they are displaying different item types
 *
 * @property item The data item this base item is bound to
 * @property layoutId The resource id of the item
 * @property uniqueId Used to compare items when diffing so RecyclerView knows how to animate
 */
abstract class BaseItem <T, VB : ViewBinding>(
    val item: T,
    val layoutId: Int,
    val uniqueId: Any
) {

    /**
     * @param view the view to bind
     * @return a view binding bound to [view]
     * */
    abstract fun initializeViewBinding(view: View): VB

    /**
     * Binds this item to the recycler view
     *
     * @param holder the view holder to get the view binding from
     * @param itemClickCallback an optional callback for when the item gets clicked
     * */
    fun bind(holder: BaseViewHolder<*>, itemClickCallback: ((BaseItem<T, VB>) -> Unit)?) {
        val specificHolder = holder as BaseViewHolder<VB>
        bind(specificHolder.binding, itemClickCallback)
    }

    /**
     * Binds this item to the recycler view
     *
     * @param binding the binding of the view
     * @param itemClickCallback an optional callback for when the item gets clicked
     * */
    abstract fun bind(binding: VB, itemClickCallback: ((BaseItem<T, VB>) -> Unit)?)

    override fun equals(other: Any?): Boolean {
        return if(other !is BaseItem<*, *>) false else other.item == item
    }

    override fun hashCode(): Int {
        return item.hashCode()
    }
}