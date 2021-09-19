package com.tolstoy.zurichat.ui.login.screens

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.data.localSource.entities.UserEntity
import com.tolstoy.zurichat.databinding.FragmentLoginBinding
import com.tolstoy.zurichat.models.LoginBody
import com.tolstoy.zurichat.models.LoginResponse
import com.tolstoy.zurichat.ui.activities.MainActivity
import com.tolstoy.zurichat.ui.login.LoginViewModel
import com.tolstoy.zurichat.util.Result
import com.tolstoy.zurichat.util.mapToEntity
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var progressDialog: ProgressDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = binding.textViewRegister
        val materialTextView = binding.materialTextView
        progressDialog = ProgressDialog(context)

        textView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerUserFragment)
        }

        materialTextView.setOnClickListener {
            findNavController().navigate(R.id.forgotPasswordFragment)
        }

        handleSignIn()
        setupObservers()
    }

    private fun handleSignIn() = with(binding) {
        buttonSignIn.setOnClickListener {
            val loginBody = LoginBody(
                email = email.text.toString().trim(),
                password = password.text.toString(),
            )
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
        Toast.makeText(context, "Please wait", Toast.LENGTH_LONG).show()
        Timber.d("Loading...")
        progressDialog.show()
    }

    private fun handleSuccess(response: LoginResponse) {
        viewModel.setLoggedIn(true)
        progressDialog.dismiss()
        response.data.user.apply {
            viewModel.saveUser(this.mapToEntity())
        }

        viewModel.saveUserResponse.observe(viewLifecycleOwner) {
            // only navigate when user data has been saved to local database
            navigateToMainScreen()
        }
    }

    private fun handleError(throwable: Throwable) {

        Toast.makeText(context, throwable.localizedMessage, Toast.LENGTH_LONG).show()
        Timber.e(throwable)
        progressDialog.dismiss()
    }

    private fun navigateToMainScreen() {
        Intent(requireContext(), MainActivity::class.java).run {
            startActivity(this)
            requireActivity().finish()
        }
        Toast.makeText(context, "You have successfully login", Toast.LENGTH_LONG).show()
    }
}

