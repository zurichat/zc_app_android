package com.tolstoy.zurichat.ui.newchannel.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSelectNewChannelBinding
import com.tolstoy.zurichat.models.NewChannel
import com.tolstoy.zurichat.ui.activities.MainActivity
import com.tolstoy.zurichat.ui.adapters.NewChannelAdapter
import com.tolstoy.zurichat.util.viewBinding
import timber.log.Timber

class SelectNewChannelFragment : Fragment(R.layout.fragment_select_new_channel) {
    private val binding by viewBinding(FragmentSelectNewChannelBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
        }
        with(binding) {
            memberButton.setOnClickListener {
                try {
                    val action =
                        SelectNewChannelFragmentDirections.actionSelectNewChannelFragmentToSelectMemberFragment()
                    findNavController().navigate(action)
                } catch (exc: Exception) {
                    Timber.e(TAG, exc.toString())
                }
            }
            memberLabel.setOnClickListener {
                try {
                    val action =
                        SelectNewChannelFragmentDirections.actionSelectNewChannelFragmentToSelectMemberFragment()
                    findNavController().navigate(action)
                } catch (exc: Exception) {
                    Timber.e(TAG, exc.toString())
                }
            }
            /*backBtn.setOnClickListener {
                requireActivity().startActivity(Intent(requireContext(), MainActivity::class.java))
            }*/
            recyclerView.apply {
                adapter = NewChannelAdapter(getNewChannel())
                layoutManager = LinearLayoutManager(requireContext())
            }
        }

    }

    //dummy data
    private fun getNewChannel(): List<NewChannel> {
        return listOf(
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_kolade_icon),
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_lux_icon),
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_mary_icon),
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_lux_icon),
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_mary_icon),
            NewChannel("Uche Klickworld", "God is great", R.drawable.ic_mary_icon),
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_mary_icon),
            NewChannel("Joseph Kalu", "God is great", R.drawable.ic_mary_icon),
            NewChannel("Olasubomi", "God is great", R.drawable.ic_lux_icon),
            NewChannel("Eniola Salami", "God is great", R.drawable.ic_lux_icon),
            NewChannel("Eniola Salami", "God is great", R.drawable.ic_lux_icon),
            NewChannel("Eniola Salami", "God is great", R.drawable.ic_lux_icon))
    }

    companion object {
        const val TAG = "SelectContactFragment"
    }
}
