package com.tolstoy.zurichat.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.data.localSource.Cache
import com.tolstoy.zurichat.databinding.FragmentConfirmAccountPasswordBinding
import com.tolstoy.zurichat.databinding.FragmentForgotPasswordBinding
import com.tolstoy.zurichat.databinding.FragmentHomeScreenBinding
import com.tolstoy.zurichat.models.LoginBody
import com.tolstoy.zurichat.models.LoginResponse
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.activities.MainActivity
import com.tolstoy.zurichat.ui.login.LoginViewModel
import com.tolstoy.zurichat.ui.organizations.utils.ZuriSharePreference
import com.tolstoy.zurichat.util.Result
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmAccountPasswordFragment : Fragment(){
    private lateinit var binding: FragmentConfirmAccountPasswordBinding
    private lateinit var user: User
    private val viewModel by viewModels<LoginViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
    private val args by navArgs<ConfirmAccountPasswordFragmentArgs>()
    private lateinit var oldUser: User

    @Inject
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentConfirmAccountPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val password = binding.confirmPassFld
        val correctPass = args.account.password
        binding.emailFld.text= args.account.email


        val btn = binding.btnConfirmpass
        btn.setOnClickListener {

            if (password.text.toString() == correctPass){
                Toast.makeText(requireContext(), "Password Correct", Toast.LENGTH_SHORT).show()
                logout()


            } else{
                Toast.makeText(requireContext(), "Password Incorrect", Toast.LENGTH_SHORT).show()
            }
        }


        observeData()
        setupObservers()

    }


    private fun observeData() {

        viewModel.logoutResponse.observe(viewLifecycleOwner, {
            when (it) {
                is Result.Success -> {
                    Toast.makeText(context, "You have been successfully logged out", Toast.LENGTH_SHORT).show()
                    updateUser()
                    handleSignIn()
//                    findNavController().navigate(R.id.action_homeScreenFragment_to_loginActivity)
//                    requireActivity().finish()
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


    private fun logout() {
        viewModel.logout()
        viewModel.clearUserAuthState()
    }



    private fun handleSignIn()  {
            val loginBody = LoginBody(
                email = args.account.email,
                password = args.account.password,
                )
            viewModel.login(loginBody)
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
    }

    private fun handleSuccess(response: LoginResponse) {


        // add user auth state to shared preference
        viewModel.saveUserAuthState(true)

        val user = response.data.user.copy(currentUser = true, password =args.account.password  )



        // add user object to room database
        viewModel.saveUser(user)
        userViewModel.addUser(user)

        val bundle = Bundle()
        bundle.putParcelable("USER", user)
        val intent = Intent(requireContext(), MainActivity::class.java)
        Cache.map.putIfAbsent("user", user)
        intent.putExtras(bundle)
        sharedPreferences.edit().putString("TOKEN",user.token).apply()
        Toast.makeText(context, "You have successfully switched Accounts", Toast.LENGTH_LONG).show()

        ZuriSharePreference(requireContext()).setString("TOKEN", user.token)
        startActivity(intent)
        requireActivity().finish()

    }

    private fun handleError(throwable: Throwable) {
        Toast.makeText(context, "Error Occurred please try again", Toast.LENGTH_LONG)
            .show()
        Timber.e(throwable)

    }

    private fun updateUser (){
       userViewModel.getCurUser().observe(viewLifecycleOwner,{
           it?.let {
               oldUser = it
           val updatedUser = oldUser.copy(currentUser = false)
           userViewModel.updateUser(updatedUser)
           }

       })

    }



}