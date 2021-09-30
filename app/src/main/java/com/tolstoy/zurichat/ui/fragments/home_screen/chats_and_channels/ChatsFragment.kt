package com.tolstoy.zurichat.ui.fragments.home_screen.chats_and_channels

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.data.remoteSource.RoomService
import com.tolstoy.zurichat.databinding.FragmentChatsBinding
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.models.Message
import com.tolstoy.zurichat.models.Room
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.fragments.home_screen.HomeScreenFragment
import com.tolstoy.zurichat.ui.fragments.home_screen.HomeScreenFragmentDirections
import com.tolstoy.zurichat.ui.fragments.home_screen.HomeScreenViewModel
import com.tolstoy.zurichat.ui.fragments.home_screen.adapters.ChatsAdapter
import com.tolstoy.zurichat.ui.profile.network.Constants
import com.tolstoy.zurichat.ui.profile.network.ProfileService
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

@AndroidEntryPoint
class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private lateinit var user : User
    private var token: String? = null
    private lateinit var userID: String
    private lateinit var originalRoomsArrayList: ArrayList<Room>


    private var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        chain.proceed(newRequest)
    }).build()

    //prepare retrofit service
    private val retrofit: Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(Constants.BASE_URL1)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val retrofitService: RoomService = retrofit
        .create(RoomService::class.java)



    private lateinit var binding: FragmentChatsBinding
    val viewModel: HomeScreenViewModel by lazy {
        (parentFragment as HomeScreenFragment).viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = requireActivity().intent.extras?.getParcelable("USER")!!
        binding = FragmentChatsBinding.bind(view)

        token = user.token

        userID = "61467ee61a5607b13c00bcf2"
        originalRoomsArrayList = ArrayList()

        getRooms(userID)
        setupObservers()
        setupUI()
    }

    private fun setupUI() = with(binding){
        viewModel.userRooms.observe(viewLifecycleOwner){ rooms ->
            val chats = rooms.map { room ->
                val otherId = room.roomUserIds.first { it != viewModel.user!!.id }
                // TODO: Resolve the name of each chat by getting the name of the user that holds this id
                ChatsAdapter.Chat("Mark", Message(senderId = otherId,
                    roomId = room.id, message = "Hey what's good"), (Math.random() * 10).toInt())
            }
            listChats.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = ChatsAdapter(chats).apply {
                    setItemClickListener { chat ->
                        val action = HomeScreenFragmentDirections
                            .actionHomeScreenFragmentToDmFragment(chat.message.roomId,
                                viewModel.user!!.id, chat.message.senderId)
                        requireView().findNavController().navigate(action)
                    }
                }
            }
        }

        fabAddChat.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_new_channel_nav_graph)
        }

        viewModel.searchQuery.observe(viewLifecycleOwner){ query ->
            Log.d(TAG, "query: $query")
            val adapter = listChats.adapter as ChatsAdapter
            adapter.differ.submitList(adapter.chats.filter {
                query.lowercase() in it.sender.lowercase()
            })
        }
    }

    private fun setupObservers() = with(viewModel){
        getRooms()
    }

    private fun getRooms(userId: String) {
        val call: Call<ArrayList<Room>> = retrofitService.getRooms(userId)

        call.enqueue(object : Callback<ArrayList<Room>> {
            override fun onResponse(call: Call<ArrayList<Room>>, response: Response<ArrayList<Room>>?) {
                if(response!!.isSuccessful) {
                    Log.i("Login Response Result", response.body()!![0].roomUserIds[0])
                    Log.i("Login Response Result", response.body()!![1].roomUserIds[1])
                    Log.i("Login Response Result", response.body().toString())

                    val roomsList = response.body()
                    if (roomsList != null) {
                        originalRoomsArrayList.addAll(roomsList)
                    }

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

            override fun onFailure(call: Call<ArrayList<Room>>, t: Throwable) {
                Timber.e(t.message.toString())
            }

        })
    }

    companion object{
        val TAG = ChatsFragment::class.simpleName
    }
}
