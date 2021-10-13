package com.zurichat.app.ui.login.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

        val backToSignin = binding.backToSig
        backToSignin.setOnClickListener {
            val action = ForgotPasswordFragmentDirections
                .actionForgotPasswordFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        val emailFld = binding.TextEditForgotPassword
        emailFld.doOnTextChanged { text, start, before, count ->
            binding.textInputForgotPassword.error = null
        }
        val btnContinue = binding.btnForgotPassword

        btnContinue.setOnClickListener  {
            if (emailFld.text.toString().isNotEmpty()) {
                resetPass(binding.TextEditForgotPassword.text.toString())
            }else{
                binding.textInputForgotPassword.error = "Enter valid email"
            }
        }

        setupPassResetObsv()
    }

    private fun resetPass(email:String){
        val passwordResetBody = PasswordReset(
            email = email
        )
        viewModel.passwordReset(passwordResetBody)
    }



    private fun setupPassResetObsv(){
        viewModel.pssswordreset.observe(viewLifecycleOwner, {
            when(it){
                is Result.Success -> {
                    val resultMsg = it.data.message
                    val email = binding.TextEditForgotPassword.text.toString()
                    Toast.makeText(requireContext()
                        , resultMsg
                        , Toast.LENGTH_SHORT).show()
                    val action = ForgotPasswordFragmentDirections
                        .actionForgotPasswordFragmentToEnterOtpFragment(email)
                    findNavController().navigate(action)

                }
                is Result.Error -> {
                    Toast.makeText(requireContext()
                        , "An error occured please check email and try again "
                        , Toast.LENGTH_SHORT).show()
                }

                is Result.Loading ->{
                    Toast.makeText(requireContext(), "please wait", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}

