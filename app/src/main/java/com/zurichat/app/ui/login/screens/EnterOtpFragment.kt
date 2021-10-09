package com.zurichat.app.ui.login.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentEnterOtpBinding
import com.zurichat.app.util.viewBinding


class EnterOtpFragment : Fragment(R.layout.fragment_enter_otp) {

    private val binding by viewBinding(FragmentEnterOtpBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val butOtp = binding.btnOtp

        butOtp.setOnClickListener(fun(it: View) {
            findNavController().navigate(R.id.confirmPasswordFragment)
        })
    }
}