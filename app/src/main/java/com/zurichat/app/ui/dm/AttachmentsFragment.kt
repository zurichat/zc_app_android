package com.zurichat.app.ui.dm

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.ProgressBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentAttachmentsBinding
import com.zurichat.app.ui.dm.adapters.AttachmentAdapter
import com.zurichat.app.ui.dm.audio.AudioAdapter
import com.zurichat.app.ui.dm.audio.AudioInfo
import java.lang.Exception

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 14-Sep-21
 */
class AttachmentsFragment : Fragment(R.layout.fragment_attachments) {

    private lateinit var binding: FragmentAttachmentsBinding
    private lateinit var media: MEDIA
    private lateinit var adapter: AttachmentAdapter
    private val viewModel by viewModels<AttachmentsViewModel>()

    private var recyclerView: RecyclerView? = null
    private var progressBar: ProgressBar? = null
    private var allAudios: ArrayList<AudioInfo>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAttachmentsBinding.bind(view)
        setupUI()
        declearation()
    }

    private fun declearation() {
        recyclerView = binding.listStorageItems

        recyclerView?.layoutManager = LinearLayoutManager(context)
        recyclerView?.setHasFixedSize(true)

        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
            } != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                101
            )
        }
        allAudios = ArrayList()
        if (allAudios!!.isEmpty()) {
            progressBar?.visibility = View.VISIBLE
            allAudios = getAllAudios()
            when (media) {
                MEDIA.AUDIO -> {
                    recyclerView?.adapter = AudioAdapter(requireContext(), allAudios!!, media)
                }
                MEDIA.IMAGE -> {
                    // TODO: Uses the data gotten from the previous screen to determine what sort of files to show
                    // TODO: but for now, for testing purposes, only images will be shown
                    viewModel.getImages(requireContext()).observe(viewLifecycleOwner) { files ->
                        binding.listStorageItems.let { list ->
                            list.layoutManager = GridLayoutManager(requireContext(), 3)
                            adapter = AttachmentAdapter(files, media)
                            list.adapter = adapter
                        }
                    }
                }
                MEDIA.DOCUMENT -> {
                    viewModel.getDoc(requireContext()).observe(viewLifecycleOwner) { files ->
                        binding.listStorageItems.let { list ->
                            list.layoutManager = LinearLayoutManager(requireContext())
                            adapter = AttachmentAdapter(files, media)
                            list.adapter = adapter
                        }
                    }
                }
            }


        }
    }

    private fun setupUI() {
        // TODO: Uses the user data gotten from the previous screen to setup the toolbar
        arguments?.let {
            media = AttachmentsFragmentArgs.fromBundle(it).media
        }

        binding.toolbarAttachments.also {
            it.inflateMenu(R.menu.attachment)
            it.setOnMenuItemClickListener { handleToolbarMenuClick(it) }
            it.setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
            it.title = getString(R.string.all_view)
        }


    }

    // function to get info about each audio retrieved
    private fun getAllAudios(): ArrayList<AudioInfo> {
        val audios = ArrayList<AudioInfo>()

        val allAudiosUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.SIZE
        )
        val cursor = context?.contentResolver?.query(allAudiosUri, projection, null, null, null)
        try {
            cursor!!.moveToFirst()
            do {
                val audio = AudioInfo()
                audio.SongUrl =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                audio.Title =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                audio.Author =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST))
                audio.Size =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                audio.Date =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED))
                audios.add(audio)
            } while (cursor.moveToNext())
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return audios


    }

    private fun handleToolbarMenuClick(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_attachment_ok) {
            findNavController().also {
                if (this::adapter.isInitialized) {
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