package com.tolstoy.zurichat.ui.organizations

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentNewWorkspaceBinding
import com.tolstoy.zurichat.models.organization_model.OrganizationCreator
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewWorkspaceFragment : Fragment(R.layout.fragment_new_workspace) {

    private val binding by viewBinding(FragmentNewWorkspaceBinding::bind)
    private val viewModel: OrganizationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarContainer.root.setNavigationOnClickListener { requireActivity().onBackPressed() }

        binding.newWorkspaceBtn.setOnClickListener {
            if (!validateOrganizationCompany() || !validateOrganizationProject()) {

            } else {
                binding.newWorkspaceBtn.setOnClickListener {
                    val organizationCreator = OrganizationCreator(
                        creator_email = "${binding.editTextCompany.toString()} " +
                                " ${binding.projectEditText.toString()}"
                    )
                    viewModel.createOrganization(organizationCreator)
                    Log.d("NewWorkspaceFragment", "onViewCreated: ${organizationCreator.creator_email}")
                }
            }
            Navigation.findNavController(it)
                .navigate(R.id.action_newWorkspaceFragment_to_nextFragment)
        }

    }

    private fun validateOrganizationCompany(): Boolean {
        if (binding.editTextCompany.length() == 0) {
            binding.editTextCompany.error = "Fill in this item"
        } else {
            binding.editTextCompany.error = null
            return true
        }
        return false
    }

    private fun validateOrganizationProject(): Boolean {
        if (binding.projectEditText.length() == 0) {
            binding.projectEditText.error = "Fill in this item"
        } else {
            binding.projectEditText.error = null
            return true
        }
        return false
    }


}