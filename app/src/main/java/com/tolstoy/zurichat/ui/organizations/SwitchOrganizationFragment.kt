package com.tolstoy.zurichat.ui.organizations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentCreateOrganizationsBinding
import com.tolstoy.zurichat.databinding.FragmentSwitchOrganizationsBinding
import com.tolstoy.zurichat.util.viewBinding

class SwitchOrganizationsFragment : Fragment(R.layout.fragment_switch_organizations) {

    private val binding by viewBinding(FragmentSwitchOrganizationsBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_switch_organizations, container, false)
    }

}