package com.zurichat.app.ui.login.screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentConfirmPasswordBinding
import com.zurichat.app.models.UpdatePassBody
import com.zurichat.app.ui.login.LoginViewModel
import com.zurichat.app.ui.organizations.utils.ZuriSharePreference
import com.zurichat.app.util.Result
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ConfirmPasswordFragment : Fragment(R.layout.fragment_confirm_password) {

    private val binding by viewBinding(FragmentConfirmPasswordBinding::bind)
    private val args by navArgs<ConfirmPasswordFragmentArgs>()
    private val viewModel by viewModels<LoginViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = binding.btnNewPassword
        val newPassFld = binding.textEditNewpass

        newPassFld.doOnTextChanged { text, start, before, count ->
            binding.textInputNewPassword.error = null
        }
        button.doOnTextChanged { text, start, before, count ->
            binding.textInputNewPassword.error = null
        }

        button.setOnClickListener{
            confirmPass()
        }

        setupUpdatePassObservers()

    }

    private fun setupUpdatePassObservers() {
        viewModel.updatePassResponse.observe(viewLifecycleOwner, { result->
            when (result) {
                is Result.Success -> { updateSuccess()}
                is Result.Error -> { updateError(result.error) }
                is Result.Loading -> { handleLoading() }
            }
        })
    }

    private fun confirmPass(){
        val newPassFld = binding.textEditNewpass
        val confPassFld = binding.textEditConfirmpassWord
        if (newPassFld.text.toString()==confPassFld.text.toString()){
            Toast.makeText(requireContext(), "Password match", Toast.LENGTH_SHORT).show()
            updatePass(newPassFld.text.toString())
        }else if (newPassFld.text.toString()!=confPassFld.text.toString()){
            binding.textInputNewPassword.error= "Password doesn't match"
        }else if (newPassFld.text.toString().isEmpty() || confPassFld.text.toString().isEmpty()){

            binding.textInputNewPassword.error = "Enter a password"
        }
    }

    private fun updatePass(password:String){
        val updatePassBody = UpdatePassBody(
            email = args.userEmail,
            password = password,
            confirm_password = password,
            code = args.otpCode
        )
        viewModel.updatePassword(updatePassBody,args.otpCode)
    }

    private fun updateSuccess(){
        ZuriSharePreference(requireActivity()).setString("Current Organization ID","")
        Toast.makeText(requireContext(), "Password change successful", Toast.LENGTH_SHORT).show()
        val action = ConfirmPasswordFragmentDirections.actionConfirmPasswordFragmentToLoginFragment()
        findNavController().navigate(action)
    }

    private fun updateError(throwable: Throwable){
        Toast.makeText(requireContext(), "Please try again", Toast.LENGTH_SHORT).show()
        Timber.e(throwable)
    }

    private fun handleLoading(){
        Toast.makeText(requireContext(), "Please wait", Toast.LENGTH_SHORT).show()
    }

}