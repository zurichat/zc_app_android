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
            Toast.makeText(requireContext(), getString(R.string.password_match), Toast.LENGTH_SHORT).show()
            updatePass(newPassFld.text.toString())
        }else if (newPassFld.text.toString()!=confPassFld.text.toString()){
            binding.textEditNewpass.error = getString(R.string.password_not_match)
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
        Toast.makeText(requireContext(), getString(R.string.fresh_sign_in),
            Toast.LENGTH_SHORT).show()
        findNavController().navigate(action)
        requireActivity().finish()
    }

    private fun updateError(throwable: Throwable){
        Toast.makeText(requireContext(), getString(R.string.please_try_again), Toast.LENGTH_SHORT).show()
        Timber.e(throwable)
    }

    private fun handleLoading(){
        Toast.makeText(requireContext(), getString(R.string.pls_wait), Toast.LENGTH_SHORT).show()
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
                        getString(R.string.log_out_success),
                        Toast.LENGTH_SHORT).show()
                    updateUser()
                    logoutSuccess()
                }
                is Result.Error -> {
                    Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    Toast.makeText(context, getString(R.string.loading), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}