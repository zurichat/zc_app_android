package com.tolstoy.zurichat.util.jsearch_view_utils

import android.view.View

abstract class JAnimationListener : AnimationListener {
    override fun onAnimationStart(view: View): Boolean {
        // No action
        return false
    }

    override fun onAnimationEnd(view: View): Boolean {
        // No action
        return false
    }

    override fun onAnimationCancel(view: View): Boolean {
        // No action
        return false
    }
}