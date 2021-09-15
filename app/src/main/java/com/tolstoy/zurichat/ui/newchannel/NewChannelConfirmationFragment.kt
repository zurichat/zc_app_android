package com.tolstoy.zurichat.ui.newchannel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentNewChannelConfirmationBinding
import com.tolstoy.zurichat.util.viewBinding

class NewChannelConfirmationFragment : Fragment(R.layout.fragment_new_channel_confirmation) {
    private val binding by viewBinding(FragmentNewChannelConfirmationBinding::bind)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}