package com.tolstoy.zurichat.ui.organizations

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentNewWorkspaceBinding
import com.tolstoy.zurichat.models.LoginBody
import com.tolstoy.zurichat.models.OrganizationModel.OrganizationCreator
import com.tolstoy.zurichat.util.Result
import com.tolstoy.zurichat.util.createProgressDialog
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NewWorkspaceFragment : Fragment(R.layout.fragment_new_workspace) {

    private val binding by viewBinding(FragmentNewWorkspaceBinding::bind)
    private val viewModel: OrganizationViewModel by viewModels()
    private lateinit var progressDialog : ProgressDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = createProgressDialog(requireContext())


        binding.toolbarContainer.root.setNavigationOnClickListener { requireActivity().onBackPressed() }

        binding.newWorkspaceBtn.setOnClickListener {
            val companyValidationResult = validateOrganizationDetails(binding.editTextCompany)
            val projectNameValidation = validateOrganizationDetails(binding.projectEditText)

            if (!companyValidationResult || !projectNameValidation) {

            }
            else
            {
                    val organizationCreator = OrganizationCreator(
                        creator_email = "${binding.editTextCompany.text}"
                    )
                    viewModel.createOrganization(organizationCreator)
                    Log.d("NewWorkspaceFragment", "onViewCreated: ${organizationCreator.creator_email}")
            }
        }

        viewModel.organizationCreator.observe(viewLifecycleOwner,{
            when(it){
                is Result.Loading -> handleLoadingState()
                is Result.Success -> handleSuccess()
                is Result.Error -> handleError(it.error)
            }
        })

    }

    private fun handleError(throwable: Throwable) {
        Toast.makeText(context, "An error occured, please try again", Toast.LENGTH_LONG)
            .show()
        Timber.e(throwable)

        progressDialog.dismiss()
    }

    private fun handleSuccess() {
        findNavController().navigate(R.id.action_newWorkspaceFragment_to_nextFragment)
        progressDialog.dismiss()
    }

    private fun handleLoadingState() {
       progressDialog.show()
    }

    private fun validateOrganizationDetails(edtText: EditText): Boolean {
        if (edtText.length() == 0) {
            edtText.error = "Fill in this item"
        } else {
            edtText.error = null
            return true
        }
        return false
    }

}