package com.zurichat.app.ui.settings.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.zurichat.app.R

class BackUpOverDialogFragment(val activity: Activity?) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Back up Over")
                .setSingleChoiceItems(
                    R.array.back_up_over,
                    0,
                    DialogInterface.OnClickListener { dialog, which ->

                    })


            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")

    }
}