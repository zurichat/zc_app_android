package com.zurichat.app.ui.organizations.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import com.zurichat.app.R
import com.zurichat.app.data.localSource.AppDatabase
import com.zurichat.app.databinding.FragmentSwitchOrganizationsBinding
import com.zurichat.app.models.User
import com.zurichat.app.models.organization_model.OrgData
import com.zurichat.app.ui.adapters.SwitchUserOrganizationAdapter
import com.zurichat.app.ui.organizations.localdatabase.OrgDao
import com.zurichat.app.ui.organizations.localdatabase.OrgRoomData
import com.zurichat.app.ui.organizations.states.UserOrganizationViewState
import com.zurichat.app.ui.organizations.utils.ZuriSharePreference
import com.zurichat.app.ui.organizations.viewmodel.UserOrganizationViewModel
import com.zurichat.app.util.ProgressLoader
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SwitchOrganizationsFragment : Fragment(R.layout.fragment_switch_organizations) {
    @Inject
    lateinit var progressLoader: ProgressLoader
    private val viewModel: UserOrganizationViewModel by viewModels()
    private val binding by viewBinding(FragmentSwitchOrganizationsBinding::bind)
    private var onOrgItemActionClicked: ((OrgData,User) -> Unit)? = null
    private lateinit var userOrgAdapter: SwitchUserOrganizationAdapter

    private lateinit var user: User
    private lateinit var database: AppDatabase
    private lateinit var orgDao: OrgDao

    private lateinit var job: Job
    private lateinit var uiScope: CoroutineScope
    private var callback: OnBackPressedCallback? = null

    private var firstTime = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments

        if (bundle != null) {
            user = bundle.getParcelable("USER")!!
        }

        if (ZuriSharePreference(requireContext()).getString("Current Organization ID","").isBlank()){
            callback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            }
            activity?.onBackPressedDispatcher?.addCallback(requireActivity(), callback!!)
        }else{
            binding.toolbar4.setNavigationIcon(R.drawable.ic_arrow_back)
            binding.toolbar4.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        job = Job()
        uiScope = CoroutineScope(Dispatchers.Main + job)

        database = Room.databaseBuilder(requireActivity().applicationContext, AppDatabase::class.java, "zuri_chat").build()
        orgDao = database.orgDao()

        binding.userEmail.text = user.email
        viewModel.setToken(getToken())
        viewModel.getUserOrganizations(emailAddress = user.email)
        uiScope.launch(Dispatchers.IO) {
            orgDao.getOrgDataWithID(user.id).let {
                uiScope.launch(Dispatchers.Main) {
                    if (it != null) {
                        if (it.orgData.isNotEmpty()){
                            firstTime = false
                            setUpViews(it.orgData)
                            binding.toolbar4.subtitle = "${it.orgData.size} Organization(s)"
                        }else{
                            firstTime = true
                        }
                    }
                    observerData()
                }
            }
            orgDao.getAllOrgData().let {

            }
        }

        binding.organizationCardView.setOnClickListener {
            val bundle1 = Bundle()
            bundle1.putParcelable("USER",user)
            findNavController().navigate(R.id.newWorkspaceFragment, bundle1)
        }
    }

    private fun getUserEmailAddress(): String? { //"glagoandrew2001@gmail.com"
        return arguments?.getString("email")
    }

    private fun getToken(): String {
        return user.token
    }

    private fun observerData() {
        lifecycleScope.launchWhenCreated {
            try {
                viewModel.userOrganizationFlow.collect {
                    when (it) {
                        is UserOrganizationViewState.Loading -> {
                            it.message?.let {
                                if (firstTime){
                                    progressLoader.show(getString(it))
                                }
                            }
                        }
                        is UserOrganizationViewState.Success -> {
                            val userOrganizations = it.userOrganizationResponseModel
                            if (firstTime){
                                progressLoader.hide()
                                snackBar(getString(it.message))
                            }
                            binding.toolbar4.subtitle = "${it.userOrganizationResponseModel?.data?.size} Organization(s)"
                            setUpViews(userOrganizations!!.data)
                            uiScope.launch(Dispatchers.IO) {
                                val orgRoomData = OrgRoomData(user.id,it.userOrganizationResponseModel.data)
                                orgDao.insertAll(orgRoomData)
                            }
                        }
                        is UserOrganizationViewState.Failure -> {
                            // progressLoader.hide()
                            if (firstTime){
                                progressLoader.hide()
                                val errorMessage = it.message
                                snackBar(errorMessage)
                            }
                        }
                        else -> {
                            // progressLoader.hide()
                            if (firstTime){
                                progressLoader.hide()
                                val errorMessage = "An Error Occurred"
                                snackBar(errorMessage)
                            }
                        }
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }

    private fun setUpViews(orgs: List<OrgData>) {
        try {
            userOrgAdapter = SwitchUserOrganizationAdapter(orgs, requireContext(),user,callback).apply {
                doOnOrgItemSelected { orgData, user ->
                    findNavController().navigateUp()
                    onOrgItemActionClicked?.invoke(orgData,user)
                    ZuriSharePreference(requireContext()).setString("Current Organization ID",orgData.id)
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
        Snackbar.make(binding.parentLayout,message, Snackbar.LENGTH_SHORT).show()
    }

}




