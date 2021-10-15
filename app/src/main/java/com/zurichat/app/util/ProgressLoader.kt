package com.zurichat.app.util

interface ProgressLoader {
    fun show(message: String? = null, cancellable: Boolean = false)
    fun hide()
}
