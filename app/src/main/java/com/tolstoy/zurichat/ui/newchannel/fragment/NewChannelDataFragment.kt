package com.tolstoy.zurichat.ui.newchannel.fragment

//import com.tolstoy.zurichat.ui.newchannel.NewChannelActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.FragmentNewChannelDataBinding
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.models.CreateChannelBodyModel
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.adapters.NewChannelMemberSelectedAdapter
import com.tolstoy.zurichat.ui.newchannel.NewChannelActivity
import com.tolstoy.zurichat.ui.newchannel.states.CreateChannelViewState
import com.tolstoy.zurichat.ui.newchannel.viewmodel.CreateChannelViewModel
import com.tolstoy.zurichat.util.ProgressLoader
import com.tolstoy.zurichat.util.ZuriSharedPreferences
import com.tolstoy.zurichat.util.getTempUser
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NewChannelDataFragment : Fragment(R.layout.fragment_new_channel_data) {
    @Inject
    lateinit var progressLoader: ProgressLoader
    private val binding by viewBinding(FragmentNewChannelDataBinding::bind)
    private val viewModel: CreateChannelViewModel by viewModels()
    private lateinit var userList: List<User>
    private var private = false
    private var channelId = ""

    private var channelsMember = ArrayList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as NewChannelActivity
        val user = activity.user
        userList = arguments?.get("Selected_user") as List<User>

        if (user != null){
            setupViewsAndListeners(user = user)
            observerData(user)
        }else{
            val user = requireContext().getTempUser()
            if (user!= null){
                setupViewsAndListeners(user = user)
                observerData(user)
            }
        }

    }

    private fun retrieveChannelOwner(user: User): String {
       return user.id
    }

    private fun setupViewsAndListeners(user: User) {
        with(binding) {
            newChannelToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            floatingActionButton.setOnClickListener {
                val name = binding.channelName.text.toString()
                val description = "$name description"
                val owner = retrieveChannelOwner(user)
                val privateValue = private
                val createChannelBodyModel = CreateChannelBodyModel(
                    description = description,
                    name = name,
                    owner = owner,
                    private = privateValue
                )
                if (channelName.text!!.isEmpty()) {
                    channelName.error = "Channel name can't be empty."
                } else {
                    viewModel.createNewChannel(createChannelBodyModel = createChannelBodyModel)
                    progressLoader.show(getString(R.string.creating_new_channel))

                }
            }

            radioGroup1.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.make_public -> {
                        private = false
                    }
                    R.id.make_private -> {
                        private = true
                    }
                }
            }
            recycler.apply {
                if (userList != null) {
                    val memberDataList = userList
                    memberDataList.forEach {
                        channelsMember.add(it.display_name)
                    }
                    val memberAdapter = NewChannelMemberSelectedAdapter(userList)
                    layoutManager =
                        LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                    adapter = memberAdapter
                }
            }
        }
    }

    private fun observerData(user: User) {
        lifecycleScope.launchWhenCreated {
            viewModel.createChannelViewFlow.collect {
                when (it) {
                    is CreateChannelViewState.Success -> {
                        val channelResponseModel = it.createChannelResponseModel
                        channelId = channelResponseModel!!._id
                        progressLoader.hide()
                        Toast.makeText(context, getString(it.message), Toast.LENGTH_LONG).show()
                        navigateToDetails(user)
                    }
                    is CreateChannelViewState.Failure -> {
                        progressLoader.hide()
                        val errorMessage = String.format(getString(it.message),
                            binding.channelName.text.toString())
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun navigateToDetails(user:User) {

        try {
            val channel = ChannelModel()
            channel.name = binding.channelName.text.toString()
            channel._id = channelId
            channel.isPrivate = private
            channel.members = channelsMember.size.toLong()

            val bundle = Bundle()
            bundle.putParcelable("USER", user)
            bundle.putParcelable("Channel", channel)
            bundle.putBoolean("Channel Joined", true)
            findNavController().navigate(R.id.channelChatFragment, bundle)
        } catch (exc: Exception) {
            exc.printStackTrace()
        }

    }
}