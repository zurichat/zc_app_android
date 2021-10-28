package com.zurichat.app.ui.dm_chat.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.zurichat.app.R
import com.zurichat.app.data.repository.orgId
import com.zurichat.app.databinding.FragmentCreateRoomBinding
import com.zurichat.app.databinding.FragmentDmBinding
import com.zurichat.app.models.User
import com.zurichat.app.ui.dm_chat.adapter.CreateRoomAdapter
import com.zurichat.app.ui.dm_chat.adapter.RoomAdapter
import com.zurichat.app.ui.dm_chat.apiservice.ApiDMService
import com.zurichat.app.ui.dm_chat.model.request.createroom.CreateRoomBody
import com.zurichat.app.ui.dm_chat.repository.Repository
import com.zurichat.app.ui.dm_chat.viewmodel.RoomViewModel
import com.zurichat.app.ui.dm_chat.viewmodel.RoomViewModelFactory
import com.zurichat.app.ui.profile.data.DataX
import com.zurichat.app.ui.profile.data.UserMemberResponse
import com.zurichat.app.ui.profile.network.Constants
import com.zurichat.app.ui.profile.network.ProfileService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber


class CreateRoomFragment : Fragment() {
    private var user : User? = null
    //token id
    private var token: String? = null
    private lateinit var orgMemId: String
    private lateinit var memId: String
    private lateinit var memberList: List<DataX>
    private lateinit var recyclerView: RecyclerView
    private lateinit var createRoomAdapter: CreateRoomAdapter
    private lateinit var member: DataX
    private lateinit var viewModelRoom: RoomViewModel
    private val roomViewModel by viewModels<RoomViewModel>()

    private lateinit var binding: FragmentCreateRoomBinding

    //setup retrofit service for getting list of members
    private var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(newRequest)
    }).build()
    //prepare retrofit service
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitService: ProfileService = retrofit
        .create(ProfileService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateRoomBinding.inflate(inflater, container, false)
        var organizationId = "6162210d8e856323d6f12110"
        getMemberId(organizationId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = binding.memberList
        memberList = ArrayList()
        var organizationId = "6162210d8e856323d6f12110"
        //getMemberId(organizationId)
        setupRecyclerView()
        selectMember()
    }

    private fun createNewChat() {
        val createButton = binding.createRoomBtn
        createButton.setOnClickListener {

        }
    }

    private fun getMemberId(orgId: String) {
        val call: Call<UserMemberResponse> = retrofitService.getUserMemberId(orgId)
        call.enqueue(object : Callback<UserMemberResponse> {
            override fun onResponse(
                call: Call<UserMemberResponse>,
                response: Response<UserMemberResponse>?
            ) {
                if(response!!.isSuccessful) {
                    Log.i("Login Response Result", response.body()!!.message)
                    Log.i("Login Response Result", "$memberList")

                    memberList = response.body()!!.data
                    memId = response.body()!!.data[0]._id
                    orgMemId = response.body()!!.data[0].org_id
                } else {
                    when(response.code()){
                        400 -> {
                            Log.e("Error 400", "invalid authorization")
                        }
                        404 -> {
                            Log.e("Error 404", "Not Found")
                        }
                        401 -> {
                            Log.e("Error 401", "No authorization or session expired")
                        }
                        else -> {
                            Log.e("Error", "Generic Error")
                        }
                    }
                }
            }
            override fun onFailure(call: Call<UserMemberResponse>, t: Throwable) {
                Timber.e(t.message.toString())
            }
        })
    }

    private fun setupRecyclerView() {
        createRoomAdapter = CreateRoomAdapter(requireActivity(), memberList)
        recyclerView.adapter = createRoomAdapter
    }
    private fun selectMember() {
        createRoomAdapter.setItemClickListener {
            val position = memberList.indexOf(it)
            member = memberList[position]

            val creatorMemId = "6162210d8e856323d6f12111"
            val otherUserMemId = member._id
            val otherUserName = member.user_name
            val orgId = member.org_id
            val roomMemberIdList = ArrayList<String>()
            roomMemberIdList.add(creatorMemId)
            roomMemberIdList.add(otherUserMemId)
            val createRoomBody = CreateRoomBody(orgId, roomMemberIdList, otherUserName)
            roomViewModel.createRoom(creatorMemId, createRoomBody)
            roomViewModel.myCreateRoomResponse.observe(viewLifecycleOwner) {response ->
                if (response.isSuccessful) {
                    val roomId: String = response.body()!!.data.ID
                    Log.i("Created Room Id", "$roomId")
                } else {
                    when (response.code()) {
                        400 -> {
                            Log.e("Error 400", "invalid authorization")
                        }
                        404 -> {
                            Log.e("Error 404", "Not Found")
                        }
                        401 -> {
                            Log.e("Error 401", "No authorization or session expired")
                        }
                        else -> {
                            Log.e("Error", "Generic Error")
                        }
                    }
                }
            }

            findNavController().navigate(R.id.action_createRoomFragment_to_homeScreenFragment)

        }
    }
}