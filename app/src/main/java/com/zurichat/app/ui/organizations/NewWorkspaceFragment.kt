package com.zurichat.app.ui.organizations

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zurichat.app.R
import com.zurichat.app.data.localSource.Cache
import com.zurichat.app.databinding.FragmentNewWorkspaceBinding
import com.zurichat.app.models.organization_model.OrganizationCreator
import com.zurichat.app.models.User
import com.zurichat.app.ui.organizations.viewmodel.OrganizationViewModel
import com.zurichat.app.util.Result
import com.zurichat.app.util.createProgressDialog
import com.zurichat.app.util.showSnackBar
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NewWorkspaceFragment : Fragment(R.layout.fragment_new_workspace) {

    private val binding by viewBinding(FragmentNewWorkspaceBinding::bind)
    private val viewModel: OrganizationViewModel by viewModels()
    private lateinit var user : User
    private lateinit var progressDialog : ProgressDialog
    @Inject
    lateinit var preference : SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * Retrieves logged in user details
         */
        val bundle = requireActivity().intent.getParcelableExtra<User>("USER")
        bundle?.let {
            user = it
        }

        progressDialog = createProgressDialog(requireContext())


        binding.toolbarContainer.root.setNavigationOnClickListener { requireActivity().onBackPressed() }

        binding.newWorkspaceBtn.setOnClickListener {
            val companyValidationResult = validateOrganizationDetails(binding.editTextCompany)
            val projectNameValidation = validateOrganizationDetails(binding.projectEditText)

            if (!companyValidationResult || !projectNameValidation) {
                binding.editTextCompany.showSnackBar("Kindly Fill in Required Fields")
            }
            else
            {
                /**
                 * Generates organizationCreator with logged in user's email
                 */
                    val organizationCreator = OrganizationCreator(
                        creator_email = user.email
                    )
                   viewModel.createOrganization(organizationCreator)
            }
        }

        // Observes result and acts accordingly
        viewModel.organizationCreator.observe(viewLifecycleOwner,{
            when(it){
                is Result.Loading -> handleLoadingState()
                is Result.Success -> {
                    handleSuccess(binding.editTextCompany.text.toString(),it.data.data.organization_id)
                    preference.edit().putString("ORG_ID", it.data.data.organization_id).apply()
                }
                is Result.Error -> handleError(it.error)
            }
        })

    }

    // function to handle error if creation og organization is not successfull
    private fun handleError(throwable: Throwable) {
        Toast.makeText(context, "An error occurred, please try again", Toast.LENGTH_LONG)
            .show()
        Timber.e(throwable)

        progressDialog.dismiss()
    }

    // function to handle next step if creation og organization is successfull
    private fun handleSuccess(organizationName : String, organizationId : String) {
        // navigates to the next fragment on success with organization name and Id
        Cache.map.putIfAbsent("orgId", organizationId)
        val action = NewWorkspaceFragmentDirections.actionNewWorkspaceFragmentToNextFragment(organizationName,organizationId)
        findNavController().navigate(action)

        progressDialog.dismiss()
    }

    // function to handle creation of organization at the loading state
    private fun handleLoadingState() {
       progressDialog.show()
       binding.editTextCompany.showSnackBar("Please Wait...")
    }

    // function to get info
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