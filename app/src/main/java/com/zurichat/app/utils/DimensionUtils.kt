package com.zurichat.app.utils

import android.content.res.Resources
import android.util.TypedValue

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * @param resources the android resource of an activity or fragment
 *
 * @return the dp equivalent of this number in int
 * */
fun Number.toDp(resources: Resources) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics
).toInt()