package com.zurichat.app.ui.newchannel.fragment

//import com.tolstoy.zurichat.ui.newchannel.NewChannelActivity
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.WorkManager
import com.zurichat.app.R
import com.zurichat.app.data.localSource.Cache
import com.zurichat.app.databinding.FragmentNewChannelDataBinding
import com.zurichat.app.models.*
import com.zurichat.app.ui.adapters.NewChannelMemberSelectedAdapter
import com.zurichat.app.ui.newchannel.states.CreateChannelViewState
import com.zurichat.app.ui.newchannel.viewmodel.CreateChannelViewModel
import com.zurichat.app.util.ProgressLoader
import com.zurichat.app.util.ZuriSharedPreferences
import com.zurichat.app.util.jsearch_view_utils.hideKeyboard
import com.zurichat.app.util.showSnackBar
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions
import kotlinx.coroutines.flow.collect
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class NewChannelDataFragment : Fragment(R.layout.fragment_new_channel_data) {
    @Inject
    lateinit var progressLoader: ProgressLoader
    private val binding by viewBinding(FragmentNewChannelDataBinding::bind)
    private val viewModel: CreateChannelViewModel by viewModels()
    private lateinit var userList: List<OrganizationMember>
    private var private = false
    private var channelId = ""
    private lateinit var organizationID: String
    private var user: User? = null
    private var selectedImageUri: Uri? = null
    private val contentResolver: ContentResolver? = null
    private var emoji: EmojIconActions? = null

    lateinit var sharedPref: SharedPreferences
    private val PREFS_NAME = "ORG_INFO"
    private val ORG_NAME = "org_name"
    private val ORG_ID = "org_id"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        organizationID = sharedPref.getString(ORG_ID, null).toString()

        user = Cache.map["user"] as? User
        userList = arguments?.get("Selected_user") as List<OrganizationMember>
        emoji =
            EmojIconActions(requireContext(), binding.root, binding.channelName, binding.emojiBtn)
        emoji!!.ShowEmojIcon()

        setupViewsAndListeners()
        observerData(view)
    }

    private fun retrieveChannelOwner(): String {
        if (user != null) {
            return user!!.id
        }
        return ""
    }

    private fun setupViewsAndListeners() {
        with(binding) {
            newChannelToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            newChannelCamera.setOnClickListener {
                selectImage()
            }

            val emojiOpen = false
            newChannelNameInput.setEndIconOnClickListener {
                //emojiBtn.visibility = View.VISIBLE
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
                if (channelName.text!!.isEmpty() || channelName.text.equals("")) {
                    Toast.makeText(requireContext(),
                        "Channel name can't be empty.",
                        Toast.LENGTH_SHORT)
                        .show()
                } else if (user?.token == null || user!!.id == "") {
                    channelName.error = "User must be logged in"
                } else {
                    hideKeyboard(it)
                    ZuriSharedPreferences(requireContext()).setInt("tracker", 0)
                    saveImage()
                    val name = channelName.text.toString()
                    val description = "$name description"
                    val owner = user!!.id
                    val privateValue = private
                    val createChannelBodyModel = CreateChannelBodyModel(
                        description = description,
                        name = name,
                        owner = owner,
                        private = privateValue
                    )
                    viewModel.createNewChannel(createChannelBodyModel = createChannelBodyModel,
                        organizationID)
                    progressLoader.show(getString(R.string.creating_new_channel))
                }
            }

            newChannelToolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            newChannelCamera.setOnClickListener {
                //selectImage()
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

            listChats.apply {
                if (userList != null) {
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

    private fun ContentResolver.getFileName(fileUri: Uri): String {
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

    private fun observerData(view: View) {
        lifecycleScope.launchWhenCreated {
            viewModel.createChannelViewFlow.collect {
                when (it) {
                    is CreateChannelViewState.Success -> {
                        if (it.createChannelResponseModel is CreateChannelResponseModel) {
                            progressLoader.hide()
                            val channelResponseModel = it.createChannelResponseModel
                            channelId = channelResponseModel._id
                            //Toast.makeText(context, getString(it.message), Toast.LENGTH_SHORT).show()
                            try {
                                sendNotification()
                            } catch (ex: Exception) {
                                ex.printStackTrace()
                            }
                            navigateToDetails()
                        }
                    }
                    is CreateChannelViewState.Failure -> {
                        progressLoader.hide()
                        val errorMessage = String.format(getString(it.message),
                            binding.channelName.text.toString())
                        view.showSnackBar(errorMessage)
                        //Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    /**
     * Send notification to users added to a channel
     */
    private fun sendNotification() {
        val workManager = WorkManager.getInstance(requireContext())
    }

    private fun navigateToDetails() {
        val channel = ChannelModel()
        channel.name = binding.channelName.text.toString()
        channel._id = channelId
        channel.isPrivate = private
        //channel.members = channelsMember.size.toLong()
        channel.members = userList.size.toLong()

        val bundle = Bundle()
        bundle.putParcelable("USER", user)
        bundle.putParcelable("Channel", channel)
        bundle.putParcelableArrayList("members", userList as ArrayList<out Parcelable>)
        bundle.putBoolean("Channel Joined", true)

        if (binding.channelName.text!!.isEmpty()) {
            binding.channelName.error = getString(R.string.channel_name_not_empty)
        } else {
            try {
                findNavController().navigate(R.id.channelChatFragment,
                    bundleOf(Pair("USER", user),
                        Pair("Channel", channel),
                        Pair("members", userList),
                        Pair("Channel Joined", true)))
            } catch (exc: Exception) {
                exc.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val tracker = ZuriSharedPreferences(requireContext()).getInt("tracker", 0)
        if (tracker == 1) {
            binding.channelName.setText("")
        }
    }

    companion object {
        const val REQUEST_IMAGE_CODE = 101
    }

}