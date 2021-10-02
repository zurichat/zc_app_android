package com.tolstoy.zurichat.ui.organizations

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.data.localSource.Cache
import com.tolstoy.zurichat.databinding.FragmentSeeYourChannelBinding
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.activities.MainActivity
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeeYourChannelFragment : Fragment(R.layout.fragment_see_your_channel) {

    private val binding by viewBinding(FragmentSeeYourChannelBinding::bind)
    private lateinit var user : User

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSeeYourChannel.setOnClickListener {
            /**
             * Retrieves logged in user details
             */
            val bundle = bundleOf("org_name" to arguments?.getString("org_name"))

            val intent = Intent(requireContext(), MainActivity::class.java)
            Cache.map.putIfAbsent("user", user)
            intent.putExtras(bundle)
            startActivity(intent)
            requireActivity().finish()
        }
    }

}