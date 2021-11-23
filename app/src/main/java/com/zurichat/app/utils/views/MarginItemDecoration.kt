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
 * @param addSpacingToStart should the spacing be added to the start of the list, defaults to true
 */
class MarginItemDecoration(
    private val space: Int,
    private val span: Int = 1,
    private val orientation: Int = GridLayoutManager.VERTICAL,
    private val addSpacingToStart: Boolean = true
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) = with(outRect) {

        val childAdapterPosition = parent.getChildAdapterPosition(view)

        if(childAdapterPosition == NO_POSITION) return

        when(orientation){
            GridLayoutManager.VERTICAL -> {
                if(addSpacingToStart && childAdapterPosition < span) top = space
                if(span != 1 && childAdapterPosition % span == 0) left = space
                if(span != 1 && childAdapterPosition % span != span - 1) right = space
            }
            else -> {
                if(addSpacingToStart && childAdapterPosition < span) left = space
                if(span != 1 && childAdapterPosition % span == 0) top = space
            }
        }

        bottom = space
    }
}