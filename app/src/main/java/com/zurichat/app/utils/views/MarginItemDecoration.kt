package com.zurichat.app.utils.views

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 21-Nov-21 at 12:57 PM
 *
 * Adds spacing between items in a recycler view
 *
 * @param space the space between each item in pixels
 * @param span the number of columns the list has, this defaults to one
 * @param orientation is the list vertical or horizontal, defaults to vertical
 */
class MarginItemDecoration(
    private val space: Int,
    private val span: Int = 1,
    private val orientation: Int = GridLayoutManager.VERTICAL
) : RecyclerView.ItemDecoration() {

    private val exclusion = mutableSetOf<Int>()

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) = with(outRect) {

        val childAdapterPosition = parent.getChildAdapterPosition(view)
        val exclude = exclusion.contains(view.id)

        if(childAdapterPosition == NO_POSITION) return

        when(orientation){
            GridLayoutManager.VERTICAL -> {
                if(childAdapterPosition < span) top = space
                if(!exclude && childAdapterPosition % span == 0){
                    left = space
//                    if(childAdapterPosition % span != span - 1) right = space
                }
            }
            else -> {
                if(childAdapterPosition < span) left = space
                if(childAdapterPosition % span == 0) top = space
            }
        }

        if(!exclude) right = space
        bottom = space
    }

    /**
     *
     * Calling this method makes sure that this view is not given a left and right margin in the list.
     * This is particularly useful for dividers in the list
     * @param viewId the id of view to exclude
     * */
    fun exclude(viewId: Int) = exclusion.add(viewId)
}