package com.tolstoy.zurichat.ui.organizations

import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.data.localSource.Cache
import com.tolstoy.zurichat.databinding.FragmentNewWorkspaceBinding
import com.tolstoy.zurichat.models.organization_model.OrganizationCreator
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.organizations.viewmodel.OrganizationViewModel
import com.tolstoy.zurichat.util.Result
import com.tolstoy.zurichat.util.createProgressDialog
import com.tolstoy.zurichat.util.showSnackBar
import com.tolstoy.zurichat.util.viewBinding
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
                    handleSuccess(binding.editTextCompany.text.toString(),it.data.data.InsertedID)
                    preference.edit().putString("ORG_ID", it.data.data.InsertedID).apply()
                }
                is Result.Error -> handleError(it.error)
            }
        })

    }

    private fun handleError(throwable: Throwable) {
        Toast.makeText(context, "An error occurred, please try again", Toast.LENGTH_LONG)
            .show()
        Timber.e(throwable)

        progressDialog.dismiss()
    }

    private fun handleSuccess(organizationName : String, organizationId : String) {
        // navigates to the next fragment on success with organization name and Id
        Cache.map.putIfAbsent("orgId", organizationId)
        val action = NewWorkspaceFragmentDirections.actionNewWorkspaceFragmentToNextFragment(organizationName,organizationId)
        findNavController().navigate(action)

        progressDialog.dismiss()
    }

    private fun handleLoadingState() {
       progressDialog.show()
       binding.editTextCompany.showSnackBar("Please Wait...")
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