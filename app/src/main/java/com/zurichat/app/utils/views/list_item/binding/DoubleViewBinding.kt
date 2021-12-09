package com.zurichat.app.utils.views.list_item.binding

import android.view.View
import androidx.viewbinding.ViewBinding

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created on 07-Dec-21 at 6:25 AM
 *
 */
class DoubleViewBinding(private val root: View, val view1: View, val view2: View): ViewBinding {
    override fun getRoot() = root
}