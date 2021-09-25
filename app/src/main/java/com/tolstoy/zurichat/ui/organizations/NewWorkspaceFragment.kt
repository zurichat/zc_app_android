package com.tolstoy.zurichat.ui.organizations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentNewWorkspaceBinding
import com.tolstoy.zurichat.util.viewBinding


class NewWorkspaceFragment : Fragment(R.layout.fragment_new_workspace) {

    private val binding by viewBinding(FragmentNewWorkspaceBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.newWorkspaceBtn.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_newWorkspaceFragment_to_nextFragment)
        }
    }


}