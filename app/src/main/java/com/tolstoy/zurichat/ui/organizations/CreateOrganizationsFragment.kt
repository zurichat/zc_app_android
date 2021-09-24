package com.tolstoy.zurichat.ui.organizations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tolstoy.zurichat.R

class CreateOrganizationsFragment : Fragment(R.layout.fragment_create_organizations) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_organizations, container, false)
    }

//    textView.setOnClickListener {
//        findNavController().navigate(R.id.action_loginFragment_to_registerUserFragment)
//    }
}