package com.tolstoy.zurichat.util

interface ProgressLoader {
    fun show(message: String? = null, cancellable: Boolean = false)
    fun hide()
}
