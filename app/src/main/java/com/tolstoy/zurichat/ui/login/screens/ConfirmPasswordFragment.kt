package com.tolstoy.zurichat.ui.login.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentConfirmPasswordBinding
import com.tolstoy.zurichat.util.viewBinding


class ConfirmPasswordFragment : Fragment(R.layout.fragment_confirm_password) {

    private val binding by viewBinding(FragmentConfirmPasswordBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_password, container, false)
    }


}