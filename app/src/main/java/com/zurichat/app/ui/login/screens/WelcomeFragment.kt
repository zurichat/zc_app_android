package com.zurichat.app.ui.login.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentWelcomeBinding
import com.zurichat.app.util.viewBinding


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