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
 * @param modify performs any other modifications to the rect, the current adapter position and rect are passed
 */
class MarginItemDecoration(
    private val space: Int,
    private val span: Int = 1,
    private val orientation: Int = GridLayoutManager.VERTICAL,
    private inline val modify: ((Int, Rect) -> Unit)? = null
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ): Unit = with(outRect) {

        val childAdapterPosition = parent.getChildAdapterPosition(view)

        if(childAdapterPosition == NO_POSITION) return

        when(orientation){
            GridLayoutManager.VERTICAL -> {
                if(childAdapterPosition < span) top = space
                if(childAdapterPosition % span == 0) left = space
//              if(childAdapterPosition % span != span - 1) right = space
            }
            else -> {
                if(childAdapterPosition < span) left = space
                if(childAdapterPosition % span == 0) top = space
            }
        }

        right = space
        bottom = space

        modify?.invoke(childAdapterPosition, this)
    }
}