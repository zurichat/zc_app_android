package com.tolstoy.zurichat.ui.login.screens

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentLoginBinding
import com.tolstoy.zurichat.databinding.FragmentSignupBinding
import com.tolstoy.zurichat.ui.activities.DMActivity
import com.tolstoy.zurichat.ui.activities.MainActivity

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = binding.textViewRegister
        val btn = binding.buttonSignUp

        textView.setOnClickListener(fun(it: View){
            findNavController().navigate(R.id.signupFragment)
        })
        btn.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            context?.startActivity(intent)
        }
    }

}