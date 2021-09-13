package com.tolstoy.zurichat.ui.login.screens

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentOtpBinding
import com.tolstoy.zurichat.ui.activities.MainActivity

class OTPFragment : Fragment() {
    private lateinit var binding: FragmentOtpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOtpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pinView = binding.pinView
        val button =   binding.otpVerifyCodeBtn
        val txtemail = binding.textViewEmail
        txtemail.setText("Please enter the code sent to\n"+arguments?.getString("email"))

        button.setOnClickListener(fun(it: View) {
            if (pinView.length() != 4) {
                pinView.error = "Pin incomplete"
                return
            } else {
                pinView.error = null
                findNavController().navigate(R.id.verifiedFragment)

            }
        })
    }
}