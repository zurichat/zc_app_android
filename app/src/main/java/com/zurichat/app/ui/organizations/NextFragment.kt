package com.zurichat.app.ui.organizations

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentAddToOrganizationBinding
import com.zurichat.app.util.generateMaterialDialog
import com.zurichat.app.util.viewBinding
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

        // Set function to each item on the toolbar when they are been clicked
        binding.toolbarAddTo.setNavigationOnClickListener { requireActivity().onBackPressed() }
        binding.apply {
            addByEmailButton.setOnClickListener(fun(_: View){
               // val action  = NextFragmentDirections.actionNextFragmentToAddByEmailFragment(organizationName, organizationId)
                //action.organizationName = organizationName
                //action.organizationId = organizationId
             //   findNavController().navigate(action)
            })
            addFromContactsButton
            addToOrganizationAppbar
            shareALinkButton.setOnClickListener {
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.putExtra(
                            Intent.EXTRA_TEXT,
                            "https://zuri.chat"
                        )
                        intent.type = "text/plain"

                        val shareIntent = Intent.createChooser(intent, null)
                        startActivity(shareIntent)
                    }
        }

        // navigate from next fragment to seeYourChannelFragment  passing in the organization name
        binding.nextTextView.setOnClickListener {
            val bundle = bundleOf("org_name" to organizationName)
           Navigation.findNavController(it).navigate(R.id.action_nextFragment_to_seeYourChannelFragment, bundle)
        }
    }


}
