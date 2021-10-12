package com.zurichat.app.ui.fragments.switch_account

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.zurichat.app.data.localSource.Cache
import com.zurichat.app.databinding.FragmentConfirmAccountPasswordBinding
import com.zurichat.app.models.*
import com.zurichat.app.ui.activities.MainActivity
import com.zurichat.app.ui.login.LoginViewModel
import com.zurichat.app.ui.organizations.utils.ZuriSharePreference
import com.zurichat.app.util.Result
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmAccountPasswordFragment : Fragment(){
    private lateinit var binding: FragmentConfirmAccountPasswordBinding
    private val viewModel by viewModels<LoginViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
    private val args by navArgs<ConfirmAccountPasswordFragmentArgs>()
    private lateinit var progressDialog: ProgressDialog

    @Inject
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentConfirmAccountPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val password = binding.confirmPassFld
        progressDialog = ProgressDialog(context)

        //set email txv to be user email address
        binding.emailFld.text= args.account.email

        //navigate to forgot password fragment with the user info
        val forgotPassTxv = binding.forgotPasswordTxv
        forgotPassTxv.setOnClickListener{
            val action =  ConfirmAccountPasswordFragmentDirections
                .actionConfirmAccountPasswordFragmentToForgetAccountPassword(args.account, args.currentUser)
            findNavController().navigate(action)
        }

        //dismiss error message on key press
        password.doOnTextChanged { text, start, before, count ->
            binding.textInputConfirmPassword.error = null
        }

        //check if password entered is correct and perform account switching process
        val btn = binding.btnConfirmpass
        btn.setOnClickListener {
            dismissKeyBoard(password)
            handleSignIn()
        }

        setupLogoutObservers()
        setupLoginObservers()
    }



    //logout current user
    private fun logout() {
        val logoutBody = LogoutBody(
            email = args.currentUser.email
        )
        viewModel.logout(logoutBody)
    }

    private fun setupLogoutObservers() {

        viewModel.logoutResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Success -> {
                    Toast.makeText(context, "You have successfully switched accounts", Toast.LENGTH_SHORT).show()
                    updateUser()
                    progressDialog.dismiss()
                }
                is Result.Error -> {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
                is Result.Loading -> {
                    progressDialog.setMessage("Loading")
                    progressDialog.show()
                }
            }
        })
    }


    private fun handleSignIn()  {
        val loginBody = LoginBody(
            email = args.account.email,
            password = binding.confirmPassFld.text.toString(),
        )
        viewModel.login(loginBody)
    }

    private fun setupLoginObservers() {
        viewModel.loginResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> handleLoading()
                is Result.Success -> handleSuccess(result.data)
                is Result.Error -> handleError(result.error)
            }
        }
    }


    private fun handleLoading() {
        progressDialog.setMessage("Loading")
        progressDialog.show()
    }

    private fun handleSuccess(response: LoginResponse) {
        dialogue(response)
    }

    private fun handleError(throwable: Throwable) {
        binding.textInputConfirmPassword.error = "Try Again"
        Toast.makeText(context, "Password Incorrect please try again", Toast.LENGTH_LONG)
            .show()
        Timber.e(throwable)
        progressDialog.dismiss()

    }


    //update user state in database
    private fun updateUser (){
        val updatedUser = args.currentUser.copy(currentUser = false)
        userViewModel.updateUser(updatedUser)
    }


    //dialogue to confirm account switch
    private fun dialogue (response: LoginResponse){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmation ")
            .setMessage("Are you sure you want to switch accounts \n(you would be logged out of current account)")
            .setNegativeButton("No"){dialog,which->
                progressDialog.dismiss()
            }
            .setPositiveButton("Yes"){dialog,which->
                viewModel.clearUserAuthState()
                loginProcess(response)
            }
            .show()
    }

    //dismiss keyboard after user presses confirm
    private fun dismissKeyBoard(view: View) {
        val keyboardManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboardManager.hideSoftInputFromWindow(view.windowToken,0)
        }

    private fun loginProcess(response: LoginResponse){
        // add user auth state to shared preference
        viewModel.saveUserAuthState(true)
        val user = response.data.user.copy(currentUser = true )

        // add user object to room database
        viewModel.saveUser(user)
        userViewModel.addUser(user)

        //bundle user object to main activity and save data to prefs to persist the login state
        val bundle = Bundle()
        bundle.putParcelable("USER", user)
        val intent = Intent(requireContext(), MainActivity::class.java)
        Cache.map.putIfAbsent("user", user)
        intent.putExtras(bundle)
        sharedPreferences.edit().putString("TOKEN",user.token).apply()
        ZuriSharePreference(requireContext()).setString("TOKEN", user.token)

        //logout previous user
        logout()

        //start homescreen activity
        startActivity(intent)
        requireActivity().finish()


    }



}