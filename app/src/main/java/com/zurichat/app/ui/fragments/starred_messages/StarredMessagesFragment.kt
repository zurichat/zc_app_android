package com.zurichat.app.ui.fragments.starred_messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zurichat.app.R
import com.zurichat.app.ZuriChatApplication
import com.zurichat.app.databinding.FragmentStarredMessagesBinding
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StarredMessagesFragment : Fragment(R.layout.fragment_starred_messages) {

    private val binding by viewBinding(FragmentStarredMessagesBinding::bind)
    private val viewModel by viewModels<StarredMessagesViewModel>()

 //   private lateinit var binding: FragmentStarredMessagesBinding
  //  private val starredMessagesViewModel by vi

//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        // Inflate the layout for this fragment
//        binding = FragmentStarredMessagesBinding.inflate(inflater, container, false)
//        return binding.root
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbarStarredMessage.setNavigationOnClickListener { requireActivity().onBackPressed() }


        val listAdapter = StarredMessageListAdapter()

        binding.starredMessagesRecycler.apply {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        viewModel.allStarredMessages.observe(
            requireActivity(),
            Observer { starredMessages ->
                starredMessages?.let { listAdapter.submitList(it) }
            })
    }
}