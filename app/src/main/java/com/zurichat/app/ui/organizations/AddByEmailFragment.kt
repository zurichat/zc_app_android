package com.zurichat.app.ui.organizations

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentAddByEmailBinding
import com.zurichat.app.util.viewBinding


class AddByEmailFragment : Fragment(R.layout.fragment_add_by_email) {

    private val binding by viewBinding(FragmentAddByEmailBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}