package com.zurichat.app.ui.screens

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentNewChannelBinding
import com.zurichat.app.models.OrganizationMember
import com.zurichat.app.util.MultiPageFragment
import com.zurichat.app.util.setClickListener
import dagger.hilt.android.AndroidEntryPoint
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created on 12-Oct-21 at 10:02 PM
 */
@AndroidEntryPoint
class NewChannelFragment: MultiPageFragment(R.layout.fragment_new_channel) {

    override val groups: List<Group> by lazy {
        with(binding){
            listOf(groupNewChannel1, groupNewChannel2, groupNewChannel3, groupNewChannel4)
        }
    }
    private val titles by lazy { resources.getStringArray(R.array.new_channel_titles) }
    private val subtitles by lazy { resources.getStringArray(R.array.new_channel_subtitles) }

    private var membersAdapter = MembersAdapter(listOf())
    private val selectedMembersAdapter by lazy { SelectedMembersAdapter(viewModel.selectedMembers) }

    private lateinit var binding: FragmentNewChannelBinding
    private val viewModel: NewChannelViewModel by viewModels()
    private val args: NewChannelFragmentArgs by navArgs()
    private var emoji: EmojIconActions? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentNewChannelBinding.bind(view)

        // display the current page
        currentPage = if(args.page > viewModel.currentPage) args.page else viewModel.currentPage
        viewModel.currentPage = currentPage
        display()

        setupObservers()
        setupUI()
    }

    override fun onPageChanged(page: Int) = with(binding){
        // update the value to the viewModel
        viewModel.currentPage = page
        // update the toolbar
        toolbarNewChannel.toolbarApp.title = titles[page]
        toolbarNewChannel.toolbarApp.subtitle = when(page){
            0 -> subtitles[page].format(membersAdapter.members.size)
            2 -> subtitles[page].format(viewModel.selectedMembers.size, membersAdapter.members.size)
            else -> subtitles[page]
        }
        // update the fab
        resources.obtainTypedArray(R.array.new_channel_fab).run {
            fabNewChannel.setImageDrawable(getDrawable(page))
            recycle()
        }
        fabNewChannel.setOnClickListener {
            if(isLastPage){
                // TODO: Create the new channel and navigate to the channel chat fragment
            }else display(Direction.FORWARD)
        }
        // update members click
        membersAdapter.listener = {
            when(page){
                0 -> {
                    // TODO: Navigate to the dm fragment
                }
                1 -> {
                    selectedMembersAdapter.add(it)
                    display(Direction.FORWARD)
                }
                2 -> {
                    selectedMembersAdapter.add(it)
                    toolbarNewChannel.toolbarApp.subtitle =
                        subtitles[page].format(viewModel.selectedMembers.size, membersAdapter.members.size)
                }
            }
        }
    }

    private fun setupUI(): Unit = with(binding){
        groupNewChannel.setClickListener{display(Direction.FORWARD)}
        textinputNewChannelName.also{
            emoji = EmojIconActions(requireContext(), root, it.editText as EmojiconEditText, imageNewChannelEmojiButton)
            emoji!!.ShowEmojIcon()
        }

        textinputNewChannelName.setEndIconOnClickListener {

        }
        listNewChannelMembers.also {
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        listNewChannelSelectedMembers.also {
            it.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            it.adapter = selectedMembersAdapter
        }
        toolbarNewChannel.toolbarApp.also {
            it.setNavigationOnClickListener {
                if(currentPage > 0) display(Direction.BACKWARD) else {
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun setupObservers(): Unit = with(viewModel){
        getMembers()
        organizationMembersResponse.observe(viewLifecycleOwner){
            it.members?.let{
                membersAdapter = MembersAdapter(it)
                binding.listNewChannelMembers.adapter = membersAdapter
                display()
            }
        }
        error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }
}