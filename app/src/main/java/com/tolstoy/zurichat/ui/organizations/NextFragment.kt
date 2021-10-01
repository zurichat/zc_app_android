package com.tolstoy.zurichat.ui.organizations

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentAddToOrganizationBinding
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
            val action = NextFragmentDirections.actionNextFragmentToSeeYourChannelFragment(
                organizationName,
                organizationId
            )
            Navigation.findNavController(it).navigate(action)
        }
    }


}