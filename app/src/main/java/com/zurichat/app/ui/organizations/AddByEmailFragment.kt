package com.zurichat.app.ui.organizations

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zurichat.app.R
import com.zurichat.app.R.drawable
import com.zurichat.app.data.remoteSource.OrganizationRetrofitClient
import com.zurichat.app.data.remoteSource.OrganizationService
import com.zurichat.app.data.remoteSource.TokenInterceptor
import com.zurichat.app.databinding.FragmentAddByEmailBinding
import com.zurichat.app.models.organization_model.RecipientEmail
import com.zurichat.app.models.organization_model.SendInviteResponse
import com.zurichat.app.models.organization_model.SentInviteBody
import com.zurichat.app.ui.adapters.RecipientEmailAdapter
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class AddByEmailFragment : Fragment(R.layout.fragment_add_by_email) {

    private lateinit var apiService: OrganizationService
    private val binding by viewBinding(FragmentAddByEmailBinding::bind)
    private lateinit var organizationName: String
    private lateinit var organizationId: String
    private lateinit var progressDialog: ProgressDialog
    private val PREFS_NAME = "USER_INFO"
    private lateinit var userPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        organizationName = arguments?.getString("org_name").toString()
        organizationId = arguments?.getString("org_id").toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.toolbarContainer.toolbar
        progressDialog = ProgressDialog(context)
        toolbar.title = "Invite"
        toolbar.subtitle = organizationName
        binding.toolbarContainer.toolbar.setLogo(drawable.ic_back)
        val logoView = toolbar.getChildAt(1)
        logoView.setOnClickListener(View.OnClickListener {
            val bundle = bundleOf(
                "organizationId" to organizationId,
                "organizationName" to organizationName)
            findNavController().navigate(R.id.action_addByEmailFragment_to_nextFragment, bundle)
        })

        val list = ArrayList<String>()
        val emails_list = ArrayList<RecipientEmail>()
        binding.recyclerview.layoutManager = LinearLayoutManager(context)

        val interceptor = TokenInterceptor()
        userPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        interceptor.setToken(userPreferences.getString("TOKEN", null))
        apiService = OrganizationRetrofitClient.createOrganizationApiService(
            true,
            tokenInterceptor = interceptor
        )

        binding.addToEmails.setOnClickListener {
            emails_list.add(RecipientEmail(binding.recipientEmailEdit.text.toString()))
            val adapter = RecipientEmailAdapter(emails_list)
            binding.recyclerview.adapter = adapter
            binding.recipientEmailEdit.text.clear()
        }

        binding.buttonSendRecipientEmail.setOnClickListener {
            for (i in 0 until emails_list.size){
                val recipientEmail = emails_list[i]
                list.add(recipientEmail.email)
            }
            if (list.size == 0){
                binding.recipientEmailEdit.setError("You must at least add one email")
            }else{
                val sendInviteBody = SentInviteBody(list)
                val call = apiService.sendInvite(organizationId, sendInviteBody)
                progressDialog.show()
                progressDialog.setTitle("Sending...")
                call.enqueue(object : Callback<SendInviteResponse?> {
                    override fun onResponse(
                        call: Call<SendInviteResponse?>,
                        response: Response<SendInviteResponse?>
                    ) {
                        if (response.code() == 200) {
                            Toast.makeText(context, "Invitation sent successfully", Toast.LENGTH_SHORT)
                                .show()
                            val bundle = bundleOf(
                                "organizationId" to organizationId,
                                "organizationName" to organizationName)
                            findNavController().navigate(R.id.action_addByEmailFragment_to_nextFragment, bundle)
                        } else {
                            Toast.makeText(context, response.body()?.message.toString(), Toast.LENGTH_SHORT).show()
                        }

                        progressDialog.dismiss()
                    }

                    override fun onFailure(call: Call<SendInviteResponse?>, t: Throwable) {

                    }

                })
            }

        }

    }

}