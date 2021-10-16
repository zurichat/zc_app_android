package com.zurichat.app.ui.settings.dialogs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentLogOutDialogBinding

class LogOutDialogFragment(val callback: () -> Unit) : DialogFragment() {
   val binding by lazy { FragmentLogOutDialogBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        with(binding) {
            no.setOnClickListener {
                dismiss()
            }
            yes.setOnClickListener {
                callback()
                dismiss()
            }
        }

        return binding.root
    }


}