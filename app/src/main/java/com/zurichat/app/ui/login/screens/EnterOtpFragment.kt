package com.zurichat.app.ui.login.screens

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zurichat.app.R
import com.zurichat.app.data.remoteSource.UsersService
import com.zurichat.app.databinding.FragmentEnterOtpBinding
import com.zurichat.app.models.PasswordReset
import com.zurichat.app.models.ResetCodeBody
import com.zurichat.app.ui.fragments.switch_account.EnterOtpACFragmentArgs
import com.zurichat.app.ui.fragments.switch_account.EnterOtpACFragmentDirections
import com.zurichat.app.ui.login.LoginViewModel
import com.zurichat.app.util.Result
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class EnterOtpFragment : Fragment(R.layout.fragment_enter_otp) {

    private val binding by viewBinding(FragmentEnterOtpBinding::bind)
    private val args by navArgs<EnterOtpFragmentArgs>()
    private val viewModel by viewModels<LoginViewModel>()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val butOtp = binding.btnOtp

        val resendCode = binding.textResendCode
        resendCode.setOnClickListener {
            viewModel.passwordReset(PasswordReset(args.userEmail))
        }

        butOtp.setOnClickListener {
            confirmCode(binding.pinView.text.toString())
        }

        setupResetCodeObservers()
        setupForgotPassObserver()
    }

    private fun confirmCode(otp: String) {
        val confirmCodeBody = ResetCodeBody(
            email = args.userEmail,
            code = otp
        )
        viewModel.verifyResetCode(confirmCodeBody)
    }

    private fun setupResetCodeObservers() {
        viewModel.resetCodeResponse.observe(viewLifecycleOwner, { result ->
            when (result) {
                is Result.Success -> {
                    resetSuccess()
                }
                is Result.Error -> {
                    resetError(result.error)
                }
                is Result.Loading -> {
                    handleLoading()
                }
            }
        })
    }

    private fun resetSuccess() {
        Toast.makeText(requireContext(), "correct code", Toast.LENGTH_SHORT).show()
        val otpCode = binding.pinView.text.toString()
        val action = EnterOtpFragmentDirections
            .actionEnterOtpFragmentToConfirmPasswordFragment(args.userEmail,otpCode)
        findNavController().navigate(action)
    }

    private fun resetError(throwable: Throwable) {
        Toast.makeText(requireContext(), "Wrong code try again", Toast.LENGTH_SHORT).show()
        Timber.e(throwable)
    }

    private fun handleLoading() {
        Toast.makeText(context, "Please wait", Toast.LENGTH_LONG).show()
    }


    private fun setupForgotPassObserver(){
        viewModel.pssswordreset.observe(viewLifecycleOwner, {
            when(it){
                is Result.Success -> {
                    val resultMsg = it.data.message
                    val email = args.userEmail
                    Toast.makeText(requireContext()
                        , resultMsg
                        , Toast.LENGTH_SHORT).show()

                }
                is Result.Error -> {
                    Toast.makeText(requireContext()
                        , "An error occurred please and try again "
                        , Toast.LENGTH_SHORT).show()
                }

                is Result.Loading ->{
                    Toast.makeText(requireContext(), "please wait", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}