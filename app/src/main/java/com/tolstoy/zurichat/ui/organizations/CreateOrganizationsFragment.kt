package com.tolstoy.zurichat.ui.organizations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentCreateOrganizationsBinding
import com.tolstoy.zurichat.util.viewBinding


class CreateOrganizationsFragment : Fragment(R.layout.fragment_create_organizations) {

    private val binding by viewBinding(FragmentCreateOrganizationsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.organizationCardView.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_createOrganizationsFragment_to_newWorkspaceFragment)
        }
    }
}