package com.zurichat.app.ui.login.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentForgotPasswordBinding
import com.zurichat.app.models.PasswordReset
import com.zurichat.app.ui.login.LoginViewModel
import com.zurichat.app.util.Result
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {
    private val binding by viewBinding(FragmentForgotPasswordBinding::bind)
    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnContinue = binding.btnForgotPassword

        btnContinue.setOnClickListener  {
            if (binding.TextEditForgotPassword.text.toString().isNotEmpty()) {
                viewModel.passwordReset(PasswordReset(binding.TextEditForgotPassword.text.toString()))
            }else{
                //toast
            }
        }

        viewModel.pssswordreset.observe(viewLifecycleOwner, {
            when(it){
              is Result.Success -> {
                  Log.d("reset","${it.data}")
                //  findNavController().navigate(R.id.enterOtpFragment)

              }
                is Result.Error -> {
                    Log.d("reset","${it.error}")
                }
            }
        })
    }

}