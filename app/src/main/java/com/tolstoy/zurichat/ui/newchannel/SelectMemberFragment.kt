package com.tolstoy.zurichat.ui.newchannel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentSelectContactBinding
import com.tolstoy.zurichat.databinding.FragmentSelectMemberBinding
import com.tolstoy.zurichat.models.MembersData
import com.tolstoy.zurichat.models.NewChannel
import com.tolstoy.zurichat.ui.activities.MainActivity
import com.tolstoy.zurichat.ui.adapters.NewChannelAdapter
import com.tolstoy.zurichat.ui.adapters.SelectMemberAdapter
import com.tolstoy.zurichat.util.viewBinding
import timber.log.Timber

class SelectMemberFragment : Fragment(R.layout.fragment_select_member) {
    private val binding by viewBinding(FragmentSelectMemberBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {
            imageButton.setOnClickListener {
                try {
                    val action =
                        SelectMemberFragmentDirections.actionSelectMemberFragmentToNewChannelSelectPersonFragment()
                    findNavController().navigate(action)
                } catch (exc: Exception) {
                    Timber.e(SelectContactFragment.TAG, exc.toString())
                }
            }
            recyclerView.apply {
                adapter = SelectMemberAdapter(selectMember(), requireContext())
                layoutManager = LinearLayoutManager(requireContext())
            }
        }



    }

    private fun selectMember(): List<MembersData> {
                return listOf(MembersData(R.drawable.dmuser, "Peculiar Umeh", "God is great"),
        MembersData(R.drawable.dmuser, "Joseph Kalu", "I am having fun"),
        MembersData(R.drawable.dmuser, "Ahmed Johnson", "God is great"),
        MembersData(R.drawable.dmuser, "Chuks Fire", "I am happy"),
        MembersData(R.drawable.dmuser, "Lukanne Godness", "Eat, code and sleep"),
        MembersData(R.drawable.dmuser, "Sammy Bloomy", "Seeing all of you"),
        MembersData(R.drawable.dmuser, "Uche Mentessa", "I will get to level ten"),
        MembersData(R.drawable.dmuser, "Oluwaseyi Oga", "God is great"),
        MembersData(R.drawable.dmuser, "John Chumme", "God is great"))


    }

    companion object {
        const val TAG = "SelectContactFragment"
    }
}