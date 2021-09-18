package com.tolstoy.zurichat.ui.login.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentWelcomeBinding
import com.tolstoy.zurichat.ui.activities.MainActivity
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private val binding by viewBinding(FragmentWelcomeBinding::bind)
    private val welcomeViewModel : WelcomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (welcomeViewModel.isLoggedIn) {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        val signInbottom = binding.signInZcBtn
        val registerButton = binding.registerZcBtn

        signInbottom.setOnClickListener  {
            findNavController().navigate(R.id.loginFragment)
            Timber.d("LoggedIn Value: ${welcomeViewModel.isLoggedIn}")
        }

        registerButton.setOnClickListener  {
            findNavController().navigate(R.id.registerUserFragment)
        }
    }

}