package com.tolstoy.zurichat.ui.organizations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSeeYourChannelBinding
import com.tolstoy.zurichat.util.viewBinding


class SeeYourChannelFragment : Fragment(R.layout.fragment_see_your_channel) {

    private val binding by viewBinding(FragmentSeeYourChannelBinding::bind)
    private val seeYourChannelFragment: SeeYourChannelFragmentArgs by navArgs()
    private lateinit var organizationName: String
    private lateinit var organizationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        organizationName = seeYourChannelFragment.organizationName
        organizationId = seeYourChannelFragment.organizationId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSeeYourChannel.setOnClickListener {
            val action =
                SeeYourChannelFragmentDirections.actionSeeYourChannelFragmentToHomeScreenFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

}