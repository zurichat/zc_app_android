package com.tolstoy.zurichat.ui.organizations

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSwitchOrganizationsBinding
import com.tolstoy.zurichat.models.organization_model.OrgRequestBody
import com.tolstoy.zurichat.ui.adapters.SwitchOrganizationAdapter
import com.tolstoy.zurichat.ui.organizations.states.UserOrganizationViewState
import com.tolstoy.zurichat.ui.organizations.viewmodel.UserOrganizationViewModel
import com.tolstoy.zurichat.ui.profile.data.UserOrganizationResponse
import com.tolstoy.zurichat.util.ProgressLoader
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class SwitchOrganizationsFragment : Fragment(R.layout.fragment_switch_organizations) {

    private val binding by viewBinding(FragmentSwitchOrganizationsBinding::bind)

    @Inject
    lateinit var progressLoader: ProgressLoader
    private val viewModel: UserOrganizationViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val requestBody = OrgRequestBody(emailAddress = getUserEmailAddress(), authToken = getToken())
        println("User token.............${requestBody.authToken}" )
        viewModel.getUserOrganizations(requestBody)

        observerData()
    }

    private fun getUserEmailAddress(): String{
        return "glagoandrew2001@gmail.com"
    }

    private fun getToken(): String {

        val token = TestFile(requireContext()).getString("TOKEN")
        println("User token......from SwitchOrg.......$token")
        return token
    }


    private fun observerData() {
        lifecycleScope.launchWhenCreated {
            viewModel.userOrganizationFlow.collect {
                when (it) {
                    is UserOrganizationViewState.Loading ->{
                        progressLoader.show("Getting User organizations")
                    }
                    is UserOrganizationViewState.Success -> {
                        val userOrganizations = it.userOrganizationResponseModel
                        progressLoader.hide()
                        Toast.makeText(context, getString(it.message), Toast.LENGTH_LONG).show()
                        populateAdapter(userOrganizations)
                    }
                    is UserOrganizationViewState.Failure -> {
                        progressLoader.hide()
                        val errorMessage = it.message
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun populateAdapter(userOrganizations: UserOrganizationResponse?) {
        with(binding){
            orgRecyclerView.apply {
                if(userOrganizations?.data != null){
                    adapter = SwitchOrganizationAdapter(userOrganizations.data, requireContext())
                    layoutManager = LinearLayoutManager(requireContext())
                }else{
                    Log.d("SwitchOrgs", "populateAdapter: Data is null")
                }
            }
        }
    }

}





