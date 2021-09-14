package com.tolstoy.zurichat.ui.login.screens

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentLoginBinding
import com.tolstoy.zurichat.models.LoginBody
import com.tolstoy.zurichat.models.LoginResponse
import com.tolstoy.zurichat.ui.activities.MainActivity
import com.tolstoy.zurichat.ui.login.LoginViewModel
import com.tolstoy.zurichat.util.Result
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var progressDialog : ProgressDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = binding.textViewRegister
        val materialTextView = binding.materialTextView
        progressDialog = ProgressDialog(context)

        textView.setOnClickListener(fun(it: View) {
            findNavController().navigate(R.id.action_loginFragment_to_registerUserFragment)
        })

        materialTextView.setOnClickListener(fun(it: View) {
            findNavController().navigate(R.id.forgotPasswordFragment)
        })


        handleSignIn()
        setupObservers()
    }

    private fun handleSignIn() = with(binding) {
        buttonSignIn.setOnClickListener {
            val loginBody = LoginBody(email = email.text.toString().trim(), password = password.text.toString())
            viewModel.login(loginBody)
        }
    }

    private fun setupObservers() {
        viewModel.loginResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> handleLoading()
                is Result.Success -> handleSuccess(result.data)
                is Result.Error -> handleError(result.error)
            }
        }
    }

    private fun handleLoading() {
        // TODO: 9/11/2021 Show loading indicator
        Timber.d("Loading...")
        progressDialog.show()
    }

    private fun handleSuccess(response: LoginResponse) {
        //val action = LoginFragmentDirections.actionLoginFragmentToMainNav(response.data.user)
       // findNavController().navigate(action)
        //findNavController().navigate(R.id.action_loginFragment_to_main_nav,bundle)

        //Starting A Activity With A Navigation Component Causes Issues With The Activity Theme.
        //Better To Sse An Intent
        progressDialog.dismiss()
        val bundle = Bundle()
        bundle.putParcelable("USER",response.data.user)
        val intent = Intent(requireContext(),MainActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun handleError(throwable: Throwable) {
        // TODO: 9/11/2021 Display error message
        Timber.e(throwable)
        progressDialog.dismiss()
    }
}

