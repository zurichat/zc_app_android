package com.tolstoy.zurichat.ui.organizations

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentAddToOrganizationBinding
import com.tolstoy.zurichat.databinding.FragmentNextBinding
import com.tolstoy.zurichat.util.viewBinding


class NextFragment : Fragment(R.layout.fragment_add_to_organization) {

    private val binding by viewBinding(FragmentAddToOrganizationBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
           addByEmailButton
            addFromContactsButton
            addToOrganizationAppbar
        }

        binding.nextTextView.setOnClickListener {
           Navigation.findNavController(it).navigate(R.id.action_nextFragment_to_seeYourChannelFragment)
        }
    }


}