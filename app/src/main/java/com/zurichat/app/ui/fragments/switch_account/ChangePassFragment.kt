package com.zurichat.app.ui.fragments.switch_account

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
import com.zurichat.app.models.LogoutBody
import com.zurichat.app.models.UpdatePassBody
import com.zurichat.app.ui.login.LoginViewModel
import com.zurichat.app.util.Result
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ChangePassFragment: Fragment(R.layout.fragment_confirm_password) {

    private val binding by viewBinding(FragmentConfirmPasswordBinding::bind)
    private val args by navArgs<ChangePassFragmentArgs>()
    private val viewModel by viewModels<LoginViewModel>()
    private val userViewModel by viewModels<UserViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = binding.btnNewPassword
        val newPassFld = binding.textEditNewpass

        newPassFld.doOnTextChanged { text, start, before, count ->
            binding.textEditNewpass.error = null
        }

        button.setOnClickListener{
            confirmPass()
        }

        setupUpdatePassObservers()
        setupLogoutObservers()
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
            binding.textEditNewpass.error = "Password doesn't match"
            }
    }

    private fun updatePass(password:String){
        val updatePassBody = UpdatePassBody(
            email = args.account.email,
            password = password,
            confirm_password = password,
            code = args.otpCode
        )
        viewModel.updatePassword(updatePassBody,args.otpCode)
    }

    private fun updateSuccess(){
        logout()
    }

    private fun logoutSuccess(){

        userViewModel.deleteUser(args.account)
        val action = ChangePassFragmentDirections.actionChangePassFragmentToLoginActivity()
        Toast.makeText(requireContext(), "password change successful\n Fresh sign in required",
            Toast.LENGTH_SHORT).show()
        findNavController().navigate(action)
        requireActivity().finish()
    }

    private fun updateError(throwable: Throwable){
        Toast.makeText(requireContext(), "Please try again", Toast.LENGTH_SHORT).show()
        Timber.e(throwable)
    }

    private fun handleLoading(){
        Toast.makeText(requireContext(), "Please wait", Toast.LENGTH_SHORT).show()
    }

    //update user state in database
    private fun updateUser (){
        val updatedUser = args.curUser.copy(currentUser = false)
        userViewModel.updateUser(updatedUser)
    }

    //logout current user
    private fun logout() {
        val logoutBody = LogoutBody(
            email = args.curUser.email
        )
        viewModel.logout(logoutBody)
        viewModel.clearUserAuthState()
    }

    private fun setupLogoutObservers() {

        viewModel.logoutResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Success -> {
                    Toast.makeText(context,
                        "You have been successfully logged out of previous account",
                        Toast.LENGTH_SHORT).show()
                    updateUser()
                    logoutSuccess()
                }
                is Result.Error -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}