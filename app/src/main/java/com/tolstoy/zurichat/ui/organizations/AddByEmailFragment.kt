package com.tolstoy.zurichat.ui.organizations

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentAddByEmailBinding
import com.tolstoy.zurichat.util.viewBinding


class AddByEmailFragment : Fragment(R.layout.fragment_add_by_email) {

    private val binding by viewBinding(FragmentAddByEmailBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}