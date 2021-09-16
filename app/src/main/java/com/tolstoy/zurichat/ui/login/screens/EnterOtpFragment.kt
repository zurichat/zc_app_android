package com.tolstoy.zurichat.ui.login.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentEnterOtpBinding
import com.tolstoy.zurichat.util.viewBinding


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