package com.zurichat.app.ui.fragments.switch_account

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentEnterOtpBinding
import com.zurichat.app.models.ResetCodeBody
import com.zurichat.app.ui.login.LoginViewModel
import com.zurichat.app.util.Result
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class EnterOtpACFragment:Fragment(R.layout.fragment_enter_otp) {

    private val binding by viewBinding(FragmentEnterOtpBinding::bind)
    private val args by navArgs<EnterOtpACFragmentArgs>()
    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val butOtp = binding.btnOtp

        val resendCode = binding.textResendCode
        resendCode.setOnClickListener {
            val action = EnterOtpACFragmentDirections
                .actionEnterOtpACFragmentToForgetAccountPassword(args.account,args.curUser)
            findNavController().navigate(action)
        }

        butOtp.setOnClickListener(fun(it: View) {
            confirmCode(binding.pinView.text.toString())

        })

        setupResetCodeObservers()
    }

    private fun confirmCode (otp:String){
        val confirmCodeBody = ResetCodeBody(
            email = args.account.email,
            code = otp
        )
        viewModel.verifyResetCode(confirmCodeBody)
    }

    private fun setupResetCodeObservers() {
        viewModel.resetCodeResponse.observe(viewLifecycleOwner, { result->
            when (result) {
                is Result.Success -> { resetSuccess()}
                is Result.Error -> { resetError(result.error) }
                is Result.Loading -> { handleLoading() }
            }
        })
    }

    private fun resetSuccess(){
        Toast.makeText(requireContext(), getString(R.string.correct_code), Toast.LENGTH_SHORT).show()
        val otpCode = binding.pinView.text.toString()
        val action = EnterOtpACFragmentDirections
            .actionEnterOtpACFragmentToChangePassFragment(args.account,otpCode,args.curUser)
        findNavController().navigate(action)
    }

    private fun resetError(throwable: Throwable){
        Toast.makeText(requireContext(), getString(R.string.wrong_code_retry), Toast.LENGTH_SHORT).show()
        Timber.e(throwable)
    }

    private fun handleLoading() {
        Toast.makeText(context, getString(R.string.pls_wait), Toast.LENGTH_LONG).show()
    }

}