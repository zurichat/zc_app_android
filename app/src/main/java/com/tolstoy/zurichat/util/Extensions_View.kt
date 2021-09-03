package com.tolstoy.zurichat.util

import android.view.View

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created 02-Sep-21 at 10:55 AM
 */
fun changeVisibility (visibility: Int, vararg view: View) = view.forEach { it.visibility = visibility }