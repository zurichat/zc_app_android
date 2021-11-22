package com.zurichat.app.utils

import android.view.View
import com.google.android.material.snackbar.Snackbar

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