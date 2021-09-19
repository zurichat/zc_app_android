package com.tolstoy.zurichat.ui.login.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentWelcomeBinding
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint



class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private val binding by viewBinding(FragmentWelcomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val signInbottom = binding.signInZcBtn
        val registerButton = binding.registerZcBtn

        signInbottom.setOnClickListener(fun(it: View) {
            findNavController().navigate(R.id.loginFragment)
        })

        registerButton.setOnClickListener(fun(it: View)  {
            findNavController().navigate(R.id.registerUserFragment)
        })
    }
}