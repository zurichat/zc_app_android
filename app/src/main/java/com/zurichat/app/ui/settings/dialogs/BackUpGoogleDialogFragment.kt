package com.zurichat.app.ui.settings.dialogs

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.zurichat.app.R

class BackUpGoogleDialogFragment(val activity: Activity?) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Back up to Google Drive")
                .setMultiChoiceItems(
                    R.array.back_up_google,
                    null,
                    DialogInterface.OnMultiChoiceClickListener { dialog, which, isChecked ->

                    })


            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")

    }
}