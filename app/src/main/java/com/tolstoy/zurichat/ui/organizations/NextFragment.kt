package com.tolstoy.zurichat.ui.organizations

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentAddToOrganizationBinding
import com.tolstoy.zurichat.databinding.FragmentNextBinding
import com.tolstoy.zurichat.models.network_response.OrganizationCreatorResponse
import com.tolstoy.zurichat.util.Result
import com.tolstoy.zurichat.util.generateMaterialDialog
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NextFragment : Fragment(R.layout.fragment_add_to_organization) {

    private val binding by viewBinding(FragmentAddToOrganizationBinding::bind)
    private val nextFragmentArgs : NextFragmentArgs by navArgs()
    private lateinit var organizationName : String
    private lateinit var organizationId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        organizationName = nextFragmentArgs.organizationName
        organizationId = nextFragmentArgs.organizationId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        generateMaterialDialog(requireActivity(),"Successful","You have successfully created" +
                " $organizationName","Dismiss",null)

        binding.toolbarAddTo.setNavigationOnClickListener { requireActivity().onBackPressed() }
        binding.apply {
            addByEmailButton
            addFromContactsButton
            addToOrganizationAppbar
            shareALinkButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "https://api.zuri.chat/organizations/${organizationId}"
                )
                intent.type = "text/plain"

                val shareIntent = Intent.createChooser(intent, null)
                startActivity(shareIntent)
            }
        }

        binding.nextTextView.setOnClickListener {
            val bundle = bundleOf("org_name" to organizationName)
            Navigation.findNavController(it).navigate(R.id.action_nextFragment_to_seeYourChannelFragment, bundle)
        }
    }


}