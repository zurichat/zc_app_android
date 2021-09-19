package com.tolstoy.zurichat.ui.login.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import com.tolstoy.zurichat.databinding.FragmentConfirmPasswordBinding
import com.tolstoy.zurichat.ui.login.LoginViewModel

@AndroidEntryPoint
class ConfirmPasswordFragment : Fragment(R.layout.fragment_confirm_password) {

    private val binding by viewBinding(FragmentConfirmPasswordBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val button = binding.btnNewPassword


        button.setOnClickListener(fun(it: View) {
            findNavController().navigate(R.id.action_confirmPasswordFragment_to_loginFragment)
        })
    }


}