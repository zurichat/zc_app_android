package com.tolstoy.zurichat.ui.newchannel.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import com.tolstoy.zurichat.databinding.FragmentNewChannelDataBinding
import com.tolstoy.zurichat.models.CreateChannelBodyModel
import com.tolstoy.zurichat.models.MembersData
import com.tolstoy.zurichat.ui.adapters.NewChannelMemberSelectedAdapter
import com.tolstoy.zurichat.ui.adapters.SelectMemberAdapter
import com.tolstoy.zurichat.ui.newchannel.states.CreateChannelViewState
import com.tolstoy.zurichat.ui.newchannel.viewmodel.CreateChannelViewModel
import com.tolstoy.zurichat.util.ProgressLoader
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class NewChannelDataFragment : Fragment(R.layout.fragment_new_channel_data) {
    @Inject
    lateinit var progressLoader: ProgressLoader
    private val binding by viewBinding(FragmentNewChannelDataBinding::bind)
    private val viewModel: CreateChannelViewModel by viewModels()
    private val args: SelectMemberFragmentArgs by navArgs()
    private var private = false
    private var channelOwner = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewsAndListeners()
        observerData()

    }

    private fun setupViewsAndListeners() {
        with(binding) {
            newChannelToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            floatingActionButton.setOnClickListener {
                val name = binding.channelName.text.toString()
                val description = "$name description"
                val owner = channelOwner
                val privateValue = private
                val createChannelBodyModel = CreateChannelBodyModel(
                    description = description,
                    name = name,
                    owner = owner,
                    private = privateValue
                )
                viewModel.createNewChannel(createChannelBodyModel = createChannelBodyModel)
                progressLoader.show("creating new channel.......")
            }

            radioGroup1.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.make_public -> {
                        private = true
                    }
                    R.id.make_private -> {
                        private = false
                    }
                }
            }
            recycler.apply {
                if (args.memberData != null) {
                    val memberDataList: List<MembersData> = args.memberData!!.toList()
                    val memberAdapter = NewChannelMemberSelectedAdapter(memberDataList)
                    layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                    adapter = memberAdapter
                }

            }

        }
    }


    private fun observerData() {
        lifecycleScope.launchWhenCreated {
            viewModel.createChannelViewFlow.collect {
                when (it) {
                    is CreateChannelViewState.Success -> {
                        progressLoader.hide()
                        Toast.makeText(context, getString(it.message), Toast.LENGTH_LONG).show()
                        navigateToDetails()
                    }
                    is CreateChannelViewState.Failure -> {
                        progressLoader.hide()
                        Toast.makeText(context, getString(it.message), Toast.LENGTH_LONG).show()
                    }

                }
            }

        }

    }

    private fun navigateToDetails() {
        val action =
            NewChannelDataFragmentDirections.actionNewChannelDataFragmentToChannelChatFragment()
        findNavController().navigate(action)
    }
}