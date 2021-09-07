package com.tolstoy.zurichat.ui.login.screens

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentEnterEmailBinding
import com.tolstoy.zurichat.ui.otp.OTP_Page
import com.tolstoy.zurichat.util.isValidEmail

class EnterEmailFragment : Fragment() {
    private lateinit var binding: FragmentEnterEmailBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnterEmailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button: Button = binding.btnVerifyEmail
        val emailContainer = binding.textInputLayout
        val bundle = Bundle()

        button.setOnClickListener {
            val email = emailContainer.editText?.text.toString()

            if (email.isValidEmail){
                bundle.putString("email", email)
                findNavController().navigate(R.id.OTPFragment, bundle)
            } else {
                emailContainer.error = "Please enter a valid email address"
            }
        }
    }
}