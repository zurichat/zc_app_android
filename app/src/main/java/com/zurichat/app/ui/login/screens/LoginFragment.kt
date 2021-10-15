package com.zurichat.app.ui.login.screens

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.zurichat.app.R
import com.zurichat.app.data.localSource.Cache
import com.zurichat.app.databinding.FragmentLoginBinding
import com.zurichat.app.models.LoginBody
import com.zurichat.app.models.LoginResponse
import com.zurichat.app.ui.activities.MainActivity
import com.zurichat.app.ui.fragments.switch_account.UserViewModel
import com.zurichat.app.ui.login.LoginViewModel
import com.zurichat.app.ui.organizations.utils.ZuriSharePreference
import com.zurichat.app.util.Result
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel by viewModels<LoginViewModel>()
    private val accModel by viewModels<UserViewModel>()
    private lateinit var prevDest: String
    private lateinit var progressDialog: ProgressDialog
    private val PREFS_NAME = "USER_INFO"
    private lateinit var userPreferences: SharedPreferences

    @Inject
    lateinit var sharedPreferences: SharedPreferences


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val textView = binding.textViewRegister
        val materialTextView = binding.materialTextView
        progressDialog = ProgressDialog(context)

        prevDest = Navigation.findNavController(view).previousBackStackEntry!!
            .destination.label.toString()

        if (prevDest == "fragment_email_verified") {
            binding.email.setText(arguments?.getString("email"))
        }


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
        progressDialog.setTitle("Loading...")
    }

    private fun handleSuccess(response: LoginResponse) {

        viewModel.saveUserAuthState(true)

        val user = response.data.user.copy(currentUser = true)

        // add user object to room database
        viewModel.saveUser(user)
        accModel.addUser(user)


        progressDialog.dismiss()
        val bundle = Bundle()
        bundle.putParcelable("USER", user)
        val intent = Intent(requireContext(), MainActivity::class.java)
        Cache.map.putIfAbsent("user", user)
        intent.putExtras(bundle)
        startActivity(intent)
        requireActivity().finish()
        sharedPreferences.edit().putString("TOKEN", user.token).apply()
        userPreferences.edit().putString("TOKEN", user.token).apply()

        Toast.makeText(context, "You have successfully logged in", Toast.LENGTH_LONG).show()
        ZuriSharePreference(requireContext()).setString("TOKEN", user.token)

    }

    private fun handleError(throwable: Throwable) {
        Toast.makeText(context, "Invalid email or password, please sign up", Toast.LENGTH_LONG)
            .show()
        Timber.e(throwable)
        progressDialog.dismiss()
    }


}

