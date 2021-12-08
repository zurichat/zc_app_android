package com.zurichat.app.utils

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.zurichat.app.R

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 *
 * Hide this view
 * */
fun View.hide() { visibility = View.GONE }

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 *
 * Show this view if it was hidden before
 * */
fun View.show() { visibility = View.VISIBLE }

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 *
 * Hide these views
 * */
fun Array<View>.hide() = forEach { it.visibility = View.GONE }

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 *
 * Show these views, if any of them was hidden before
 * */
fun Array<View>.show() = forEach { it.visibility = View.VISIBLE }

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 *
 * Display a snackbar with the message
 *
 * @param message the string to show
 * @param duration how long the should the message be displayed, the default is <code>Snackbar.LENGTH_SHORT</code>
 * */
fun View.showSnackbar(message: String, duration: Int = Snackbar.LENGTH_SHORT){
    Snackbar.make(this, message, duration).show()
}

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 *
 * Display a snackbar with the string resource
 *
 * @param resId the resource id of the string to display
 * @param duration how long the should the message be displayed, the default is <code>Snackbar.LENGTH_SHORT</code>
 * */
fun View.showSnackbar(resId: Int, duration: Int = Snackbar.LENGTH_SHORT){
    Snackbar.make(this, resId, duration).show()
}

fun MaterialButton.asFabButton(
    buttonIcon: Drawable?,
    size: Int,
    iconSpacing: Int = 2.dp(resources)){

    layoutParams = LinearLayout.LayoutParams(size, size)
    icon = buttonIcon
    iconSize = size - iconSpacing
    iconGravity = MaterialButton.ICON_GRAVITY_TEXT_START
    iconPadding = 0
    setPadding(0)
    cornerRadius = size
    insetBottom = 0
    insetTop = 0
    setIconTintResource(android.R.color.white)
    setBackgroundColor(context.colorFromAttr(R.attr.colorAccent))
}