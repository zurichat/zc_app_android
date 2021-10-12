package com.zurichat.app.ui.organizations

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentSeeYourChannelBinding
import com.zurichat.app.util.viewBinding


class SeeYourChannelFragment : Fragment(R.layout.fragment_see_your_channel) {

    private val binding by viewBinding(FragmentSeeYourChannelBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

// navigate from see your channel fragment to mainActivity passing in the organization
        binding.btnSeeYourChannel.setOnClickListener {
            try{
                val bundle = bundleOf("org_name" to arguments?.getString("org_name"))
                Navigation.findNavController(it).navigate(R.id.action_seeYourChannelFragment_to_mainActivity, bundle)
            }catch (exc:Exception){
                exc.printStackTrace()
            }
        }
    }

}