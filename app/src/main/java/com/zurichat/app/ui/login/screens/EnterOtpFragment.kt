package com.zurichat.app.ui.login.screens

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.zurichat.app.R
import com.zurichat.app.data.remoteSource.UsersService
import com.zurichat.app.databinding.FragmentEnterOtpBinding
import com.zurichat.app.util.viewBinding


class EnterOtpFragment : Fragment(R.layout.fragment_enter_otp) {

    private val binding by viewBinding(FragmentEnterOtpBinding::bind)

    private lateinit var usersService: UsersService
    private lateinit var navController: NavController
    private lateinit var  bundle: Bundle
    private lateinit var btnVerifyEmail: Button


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val butOtp = binding.btnOtp

        butOtp.setOnClickListener(fun(it: View) {
            findNavController().navigate(R.id.confirmPasswordFragment)
        })
    }
}