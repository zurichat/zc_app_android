package com.tolstoy.zurichat.ui.organizations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSeeYourChannelBinding
import com.tolstoy.zurichat.util.viewBinding


class SeeYourChannelFragment : Fragment(R.layout.fragment_see_your_channel) {

    private val binding by viewBinding(FragmentSeeYourChannelBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSeeYourChannel.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_seeYourChannelFragment_to_homeScreenFragment)
        }

    }

}