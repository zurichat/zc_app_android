package com.zurichat.app.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat


private fun Context.typedValueFromAttr(attr: Int) = TypedValue().also {
    theme.resolveAttribute(attr, it, true)
}

@ColorInt
fun Context.colorFromAttr(attr: Int) =
    ContextCompat.getColor(this, typedValueFromAttr(attr).resourceId)

fun Context.drawableFromAttr(attr: Int) =
    ContextCompat.getDrawable(this, typedValueFromAttr(attr).resourceId)