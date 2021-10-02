package com.tolstoy.zurichat

import android.content.*
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import com.tolstoy.zurichat.databinding.FragmentShareLinkBinding
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShareLinkFragment : Fragment() {


    private val binding by viewBinding(FragmentShareLinkBinding::bind)
    private lateinit var organizationID: String

    private lateinit var user: User
    private val PREFS_NAME = "ORG_INFO"

    @Inject
    lateinit var preference: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        user = requireActivity().intent.extras?.getParcelable("USER")!!
        preference = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share_link, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        currentOrgName = arguments?.getString(ORG_NAME).toString()
//        organizationID = arguments?.getString(ORG_ID).toString()

        organizationID = preference.getString("ORG_ID", "") ?: ""





        binding.linkGenerated.text = "https://api.zuri.chat/organizations/${organizationID}"


        binding.copyBtn.setOnClickListener {
            val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("TextView", binding.linkGenerated.text.toString())
            clipboard.setPrimaryClip(clip)
            Toast.makeText(context, "link copied", Toast.LENGTH_SHORT).show()
        }

        binding.toolbarAddToSL.setNavigationOnClickListener { requireActivity().onBackPressed() }
        binding.apply {
            addToOrganizationAppbar
            shareALinkButtonSL.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "https://api.zuri.chat/organizations/${organizationID}"
                )
                intent.type = "text/plain"

                val shareIntent = Intent.createChooser(intent, null)
                startActivity(shareIntent)
            }
        }
    }


}