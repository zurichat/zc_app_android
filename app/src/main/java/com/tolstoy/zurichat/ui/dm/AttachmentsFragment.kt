package com.tolstoy.zurichat.ui.dm

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentAttachmentsBinding
import com.tolstoy.zurichat.ui.dm.adapters.AttachmentAdapter

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 14-Sep-21
 */
class AttachmentsFragment: Fragment(R.layout.fragment_attachments) {

    private val binding by lazy { FragmentAttachmentsBinding.bind(requireView()) }
    private val viewModel by viewModels<AttachmentsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() = binding.also{
        // TODO: Uses the user data gotten from the previous screen to setup the toolbar

        // TODO: Uses the data gotten from the previous screen to determine what sort of files to show
        // TODO: but for now, for testing purposes, only images will be shown
        viewModel.getImages(requireContext()).observe(viewLifecycleOwner){ files ->
            it.listStorageItems.adapter = AttachmentAdapter(files, MEDIA.IMAGE)
        }
    }
}