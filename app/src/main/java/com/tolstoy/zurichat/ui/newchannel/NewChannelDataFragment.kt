package com.tolstoy.zurichat.ui.newchannel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentNewChannelDataBinding
import com.tolstoy.zurichat.util.viewBinding

class NewChannelDataFragment : Fragment(R.layout.fragment_new_channel_data) {
    private val binding by viewBinding(FragmentNewChannelDataBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}