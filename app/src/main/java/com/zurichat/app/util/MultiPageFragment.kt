package com.zurichat.app.util

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 12-Oct-21 at 10:07 PM
 *
 * This is a fragment implementation that tries to reduce the amount of files
 * that contain identical views into the same fragment and use groups to separate them
 */
abstract class MultiPageFragment(layout: Int): Fragment(layout) {
    abstract val groups: List<Group>

    protected var currentPage: Int = 0
    set(value) {
        if(value >= 0 && value < groups.size) field = value
    }

    val isLastPage: Boolean get() = currentPage == groups.lastIndex

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(currentPage > 0) display(Direction.BACKWARD) else {
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })
    }

    abstract fun onPageChanged(page: Int)

    fun display(direction: Direction? = null){
        direction?.let{
            when(it){
                Direction.FORWARD -> if(currentPage++ < groups.lastIndex)
                    for(i in currentPage - 1 .. 0) changeVisibility(View.GONE, groups[i])
                Direction.BACKWARD -> if(currentPage-- > 0)
                    for(i in currentPage + 1 .. groups.lastIndex) changeVisibility(View.GONE, groups[i])
            }
        }
        changeVisibility(View.VISIBLE, groups[currentPage])
        onPageChanged(currentPage)
    }

    enum class Direction{
        FORWARD, BACKWARD
    }
}