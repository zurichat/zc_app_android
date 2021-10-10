package com.zurichat.app.ui.organizations.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentSwitchOrganizationsBinding
import com.zurichat.app.models.organization_model.Data
import com.zurichat.app.ui.adapters.SwitchUserOrganizationAdapter
import com.zurichat.app.ui.organizations.states.UserOrganizationViewState
import com.zurichat.app.ui.organizations.utils.ZuriSharePreference
import com.zurichat.app.ui.organizations.viewmodel.UserOrganizationViewModel
import com.zurichat.app.util.ProgressLoader
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class SwitchOrganizationsFragment : Fragment(R.layout.fragment_switch_organizations) {

    @Inject
    lateinit var progressLoader: ProgressLoader
    private val viewModel: UserOrganizationViewModel by viewModels()
    private val binding by viewBinding(FragmentSwitchOrganizationsBinding::bind)
    private var onOrgItemActionClicked: ((Data) -> Unit)? = null
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
                        snackBar(getString(it.message))
                        setUpViews(userOrganizations!!.data)
                    }
                    is UserOrganizationViewState.Failure -> {
                        progressLoader.hide()
                        val errorMessage = it.message
                        snackBar(errorMessage)
                    }
                }
            }
        }
    }

    private fun setUpViews(orgs: List<Data>) {

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
        } catch (e: NullPointerException){
            Toast.makeText(context, "User has no organization", Toast.LENGTH_LONG).show()
        }

    }
    private fun snackBar(message:String){
        Snackbar.make(binding.parentLayout,message, Snackbar.LENGTH_SHORT)
            .show()
    }
}



private const val TOKEN = "TOKEN"





