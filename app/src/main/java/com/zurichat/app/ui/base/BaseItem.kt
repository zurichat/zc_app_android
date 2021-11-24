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
 * @property itemClickCallback The function called when this item is clicked
 */
abstract class BaseItem <T, VB : ViewBinding>(
    val item: T,
    val layoutId: Int,
    val uniqueId: Any,
    val itemClickCallback: ((T) -> Unit)? = null
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
     * */
    fun bind(holder: BaseViewHolder<*>) {
        val specificHolder = holder as BaseViewHolder<VB>
        bind(specificHolder.binding)
    }

    /**
     * Binds this item to the recycler view
     *
     * @param binding the binding of the view
     * */
    abstract fun bind(binding: VB)

    override fun equals(other: Any?): Boolean {
        return if(other !is BaseItem<*, *>) false else other.item == item
    }

    override fun hashCode(): Int {
        return item.hashCode()
    }
}