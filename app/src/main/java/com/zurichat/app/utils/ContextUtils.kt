package com.zurichat.app.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources

/**
 *
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 *
 */
@ColorInt
fun Context.colorFromAttr(attr: Int) = with(TypedValue()) {
    theme.resolveAttribute(attr, this, true)
    data
}

fun Context.drawableFromAttr(attr: Int) = with(theme.obtainStyledAttributes(IntArray(attr))) {
    AppCompatResources.getDrawable(this@drawableFromAttr, getResourceId(0,0))
}