package com.tolstoy.zurichat.ui.newchannel.fragment

//import com.tolstoy.zurichat.ui.newchannel.NewChannelActivity
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
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
import hani.momanii.supernova_emoji_library.Actions.*
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions
import kotlinx.coroutines.flow.collect
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
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

    // private var user:User?= null
    private var selectedImageUri: Uri? = null
    private val contentResolver: ContentResolver? = null
    private var channelsMember = ArrayList<String>()
    private var emoji: EmojIconActions? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as NewChannelActivity
        // user = activity.user
        userList = arguments?.get("Selected_user") as List<User>
        emoji = EmojIconActions(requireContext(), binding.root,
            binding.channelName, binding.emojiBtn)
        emoji!!.ShowEmojIcon()

        val user = activity.user
        userList = arguments?.get("Selected_user") as List<User>

        if (user != null) {
            setupViewsAndListeners(user = user)
            observerData(user)
        } else {
            val defaultUser = requireContext().getTempUser()
            if (defaultUser != null) {
                setupViewsAndListeners(user = defaultUser)
                observerData(defaultUser)
            }
        }

        binding.newChannelToolbar.setOnClickListener {
            activity.finish()
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

            newChannelCamera.setOnClickListener {
                selectImage()
            }

            newChannelNameInput.setEndIconOnClickListener {
                emojiBtn.visibility = View.VISIBLE

                emoji?.setKeyboardListener(object : EmojIconActions.KeyboardListener {
                    override fun onKeyboardOpen() {
                        Log.e("Keyboard", "open")
                    }

                    override fun onKeyboardClose() {
                        Log.e("Keyboard", "close")
                    }
                })
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
                if (channelName.text!!.isEmpty() || channelName.text.equals("")) {
                    Toast.makeText(requireContext(), "Channel name can't be empty.", Toast.LENGTH_SHORT).show()
                }
                else if (user?.token == null || user!!.id == "") {
                    channelName.error = "User must be logged in"
                } else {
                    saveImage()
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

    private fun saveImage() {


        val parcelFileDescriptor =
            contentResolver?.openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
    }

    fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }

    private fun selectImage() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_IMAGE_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CODE -> {
                    selectedImageUri = data?.data
                    binding.newChannelCamera.setImageURI(selectedImageUri)
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

    private fun navigateToDetails(user: User) {

        try {
            val channel = ChannelModel()
            channel.name = binding.channelName.text.toString()
            channel._id = channelId
            channel.isPrivate = private
            channel.members = channelsMember.size.toLong()

            val bundle = Bundle()
            bundle.putParcelable("USER", user)
            bundle.putParcelable("Channel", channel)
            bundle.putParcelableArrayList("selected_members", ArrayList(userList))
            bundle.putBoolean("Channel Joined", true)

            if (binding.channelName.text!!.isEmpty()) {
                binding.channelName.error = "Channel name can't be empty."
            } else {
                findNavController().navigate(R.id.channelChatFragment, bundle)
            }

            /* val action =
                 NewChannelDataFragmentDirections.actionNewChannelDataFragmentToChannelChatFragment(
                     members = members.toTypedArray(),
                     channelName = channelName,
                     user = user,
                     channelStatus = private,
                     channelId = channelId
                 )
             findNavController().navigate(action)*/
//            val bundle = Bundle()
//            bundle.putParcelable("USER", user)
//            bundle.putParcelable("Channel", channel)
//            bundle.putBoolean("Channel Joined", true)
//            findNavController().navigate(R.id.channelChatFragment, bundle)
        } catch (exc: Exception) {
            exc.printStackTrace()
        }

    }

    companion object {
        const val REQUEST_IMAGE_CODE = 101
    }
}