package com.tolstoy.zurichat.ui.login.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentForgotPasswordBinding
import com.tolstoy.zurichat.models.PasswordReset
import com.tolstoy.zurichat.ui.login.LoginViewModel
import com.tolstoy.zurichat.util.Result
import com.tolstoy.zurichat.util.viewBinding
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