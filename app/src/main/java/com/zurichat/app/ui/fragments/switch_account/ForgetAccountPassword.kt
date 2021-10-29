package com.zurichat.app.ui.fragments.switch_account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentForgetAccountPassBinding
import com.zurichat.app.models.PasswordResetBody
import com.zurichat.app.ui.login.LoginViewModel
import com.zurichat.app.util.Result
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgetAccountPassword : Fragment(R.layout.fragment_forget_account_pass) {
    private val binding by viewBinding(FragmentForgetAccountPassBinding::bind)
    private val viewModel by viewModels<LoginViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
    private val args by navArgs<ForgetAccountPasswordArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnContinue = binding.btnForgotPassword
        binding.userEmailTxv.text = args.account.email

        btnContinue.setOnClickListener  {
            dialogue()
        }

        viewModel.passwordReset.observe(viewLifecycleOwner, {
            when(it){
                is Result.Success -> {
                    val message = it.data.message
                    success(message)
                }
                is Result.Error -> {
                    Toast.makeText(requireContext(), getString(R.string.please_try_again), Toast.LENGTH_SHORT).show()
                }
                is Result.Loading ->{
                    Toast.makeText(requireContext(), getString(R.string.pls_wait), Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun dialogue (){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.confirm))
            .setMessage(getString(R.string.sure_reset_password))
            .setNegativeButton(getString(R.string.no)){dialog,which->

            }
            .setPositiveButton(getString(R.string.yes)){dialog,which->
                viewModel.passwordReset(PasswordResetBody(args.account.email))
            }
            .show()
    }

    private fun success(message:String){
        val email = args.account.email
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

        val action = ForgetAccountPasswordDirections.actionForgetAccountPasswordToEnterOtpACFragment(args.account,args.curUser)
        findNavController().navigate(action)
//        requireActivity().finish()
    }

}