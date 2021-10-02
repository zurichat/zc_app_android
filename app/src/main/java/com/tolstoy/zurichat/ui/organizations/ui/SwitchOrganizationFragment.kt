package com.tolstoy.zurichat.ui.organizations.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSwitchOrganizationsBinding
import com.tolstoy.zurichat.models.network_response.UserOrganizationModel
import com.tolstoy.zurichat.ui.adapters.SwitchUserOrganizationAdapter
import com.tolstoy.zurichat.ui.organizations.states.UserOrganizationViewState
import com.tolstoy.zurichat.ui.organizations.utils.ZuriSharePreference
import com.tolstoy.zurichat.ui.organizations.viewmodel.UserOrganizationViewModel
import com.tolstoy.zurichat.util.ProgressLoader
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class SwitchOrganizationsFragment : Fragment(R.layout.fragment_switch_organizations) {

    @Inject
    lateinit var progressLoader: ProgressLoader
    private val viewModel: UserOrganizationViewModel by viewModels()
    private val binding by viewBinding(FragmentSwitchOrganizationsBinding::bind)
    private var onOrgItemActionClicked: ((UserOrganizationModel.Data) -> Unit)? = null
    private lateinit var userOrgAdapter: SwitchUserOrganizationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setToken(getToken())
        viewModel.getUserOrganizations(emailAddress = getUserEmailAddress()!!)

        observerData()
    }

    private fun getUserEmailAddress(): String? { //"glagoandrew2001@gmail.com"
        return arguments?.getString("email")
    }

    private fun getToken(): String {
        return ZuriSharePreference(requireContext()).getString(TOKEN)
    }

    private fun observerData() {
        lifecycleScope.launchWhenCreated {
            viewModel.userOrganizationFlow.collect {
                when (it) {
                    is UserOrganizationViewState.Loading -> {
                        it.message?.let {
                            progressLoader.show(getString(it))
                        }
                    }
                    is UserOrganizationViewState.Success -> {
                        val userOrganizations = it.userOrganizationResponseModel
                        progressLoader.hide()
                        Toast.makeText(context, getString(it.message), Toast.LENGTH_LONG).show()
                        setUpViews(userOrganizations!!.data)
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

    private fun setUpViews(orgs: List<UserOrganizationModel.Data>) {

        try {
            userOrgAdapter = SwitchUserOrganizationAdapter(orgs, requireContext()).apply {
                doOnOrgItemSelected {
                    findNavController().navigateUp()
                    onOrgItemActionClicked?.invoke(it)

                }
            }
            binding.orgRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = userOrgAdapter
            }
            Toast.makeText(context, "user organizations retrieved successfully", Toast.LENGTH_LONG).show()
        } catch (e: NullPointerException){
            Toast.makeText(context, "User has no organization", Toast.LENGTH_LONG).show()
        }

    }
}

private const val TOKEN = "TOKEN"





