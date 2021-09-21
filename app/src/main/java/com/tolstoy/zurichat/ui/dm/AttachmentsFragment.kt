package com.tolstoy.zurichat.ui.dm

import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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
    private lateinit var media: MEDIA
    private lateinit var adapter: AttachmentAdapter
    private val viewModel by viewModels<AttachmentsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAttachmentsBinding.bind(view)
        setupUI()
    }

    private fun setupUI() {
        // TODO: Uses the user data gotten from the previous screen to setup the toolbar
        arguments?.let {
            media = AttachmentsFragmentArgs.fromBundle(it).media
        }

        binding.toolbarAttachments.also {
            it.inflateMenu(R.menu.attachment)
            it.setOnMenuItemClickListener {handleToolbarMenuClick(it)}
            it.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
            it.title = getString(R.string.all_view, media.name.lowercase())
        }

        // TODO: Uses the data gotten from the previous screen to determine what sort of files to show
        // TODO: but for now, for testing purposes, only images will be shown

        viewModel.getImages(requireContext()).observe(viewLifecycleOwner){ files ->
            binding.listStorageItems.let { list ->
                list.layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = AttachmentAdapter(files, media)
                list.adapter = adapter
            }
        }
    }

    private fun handleToolbarMenuClick(item: MenuItem): Boolean{
        if(item.itemId == R.id.action_attachment_ok){
            findNavController().also {
                if(this::adapter.isInitialized) {
                    it.previousBackStackEntry?.savedStateHandle
                        ?.getLiveData<Attachment>(Attachment.TAG)
                        ?.value = Attachment(adapter.selected, media)
                }
                it.popBackStack()
            }
        }
        return true
    }

    data class Attachment(val selected: List<Uri>, val media: MEDIA){
        companion object{
            val TAG = this::class.simpleName.toString()
        }
    }
}