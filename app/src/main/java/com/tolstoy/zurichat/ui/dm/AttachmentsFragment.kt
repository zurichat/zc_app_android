package com.tolstoy.zurichat.ui.dm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentAttachmentsBinding
import com.tolstoy.zurichat.ui.dm.adapters.AttachmentAdapter

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 14-Sep-21
 */
class AttachmentsFragment: Fragment(R.layout.fragment_attachments) {

    private lateinit var binding: FragmentAttachmentsBinding
    private val viewModel by viewModels<AttachmentsViewModel>()
    private lateinit var adapter: AttachmentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAttachmentsBinding.bind(view)
        setupUI()
    }

    private fun setupUI() {
        // TODO: Uses the user data gotten from the previous screen to setup the toolbar

        // TODO: Uses the data gotten from the previous screen to determine what sort of files to show
        // TODO: but for now, for testing purposes, only images will be shown

        viewModel.getImages(requireContext()).observe(viewLifecycleOwner){ files ->
            binding.listStorageItems.let { list ->
                list.layoutManager = GridLayoutManager(requireContext(), 3)
                list.adapter = AttachmentAdapter(files, MEDIA.IMAGE)
            }
        }
    }
}