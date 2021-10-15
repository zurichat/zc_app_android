package com.zurichat.app.util

import android.view.View
import androidx.constraintlayout.widget.Group

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created 02-Sep-21 at 10:55 AM
 */
fun changeVisibility (visibility: Int, vararg view: View) = view.forEach { it.visibility = visibility }

fun Group.setClickListener(listener: View.OnClickListener) = referencedIds.forEach {
    rootView.findViewById<View>(it).setOnClickListener(listener)
}