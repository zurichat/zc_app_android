package com.zurichat.app.ui.organizations

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zurichat.app.R
import com.zurichat.app.data.remoteSource.OrganizationRetrofitClient
import com.zurichat.app.data.remoteSource.OrganizationService
import com.zurichat.app.data.remoteSource.TokenInterceptor
import com.zurichat.app.databinding.FragmentAddByEmailBinding
import com.zurichat.app.models.organization_model.SendInviteResponse
import com.zurichat.app.models.organization_model.SentInviteBody
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
    private val PREFS_NAME = "USER_INFO"
    private lateinit var userPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        organizationName = arguments?.getString("org_name").toString()
        organizationId = arguments?.getString("org_id").toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var list = ArrayList<String>()
        val interceptor = TokenInterceptor()
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_list_item_1, list)
        userPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        interceptor.setToken(userPreferences.getString("TOKEN", null))
        apiService = OrganizationRetrofitClient.createOrganizationApiService(
            true,
            tokenInterceptor = interceptor
        )

        binding.addToEmails.setOnClickListener {
            list.add(binding.recipientEmailEdit.text.toString())
            binding.emailsList.adapter = adapter
            binding.recipientEmailEdit.text.clear()

        }

        binding.buttonSendRecipientEmail.setOnClickListener {
            val sendInviteBody = SentInviteBody(list)
            val call = apiService.sendInvite(organizationId, sendInviteBody)
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
                        Toast.makeText(
                            context,
                            response.body()?.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                }

                override fun onFailure(call: Call<SendInviteResponse?>, t: Throwable) {
                    
                }

            })
        }

    }

}