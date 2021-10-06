package com.zurichat.app.ui.login.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.zurichat.app.R
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import com.zurichat.app.databinding.FragmentConfirmPasswordBinding

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