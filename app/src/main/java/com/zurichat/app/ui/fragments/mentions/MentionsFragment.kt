package com.zurichat.app.ui.fragments.mentions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.zurichat.app.data.localSource.AppDatabase
import com.zurichat.app.databinding.FragmentMentionsBinding
import com.zurichat.app.models.User
import com.zurichat.app.ui.add_channel.BaseItem
import com.zurichat.app.ui.add_channel.BaseListAdapter
import com.zurichat.app.ui.fragments.channel_chat.localdatabase.ChannelMessagesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class MentionsFragment : Fragment() {
    private lateinit var binding: FragmentMentionsBinding
    private lateinit var database: AppDatabase
    private lateinit var channelMessagesDao: ChannelMessagesDao

    private lateinit var user : User
    private lateinit var organizationID: String

    private lateinit var job: Job
    private lateinit var uiScope: CoroutineScope

    private lateinit var mentionsListAdapter : BaseListAdapter
    private lateinit var mentionsList : ArrayList<Mention>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMentionsBinding.inflate(inflater, container, false)
        database = Room.databaseBuilder(requireActivity().applicationContext, AppDatabase::class.java, "zuri_chat").build()
        channelMessagesDao = database.channelMessagesDao()

        user = requireActivity().intent.extras?.getParcelable("USER")!!
        organizationID = "614679ee1a5607b13c00bcb7"

        job = Job()
        uiScope = CoroutineScope(Dispatchers.Main + job)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mentionsList = ArrayList()
        binding.channelToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        mentionsListAdapter = BaseListAdapter { channelItem ->

        }
        binding.mentionsRecyclerView.adapter = mentionsListAdapter

        uiScope.launch(Dispatchers.IO){
            channelMessagesDao.getAllChannelMessages().let { list ->
                list.forEach { allChannelMessages ->
                    val dataList = allChannelMessages.data
                    dataList.forEach {
                        val data = it
                        if ((data.content.toString()).contains("@"+user.display_name,true)){
                            val mention = Mention(data.channel_id,data.content.toString(),data.user_id.toString(),data._id.toString(),data.timestamp.toString())
                            mentionsList.add(mention)
                        }
                    }
                }
                uiScope.launch(Dispatchers.Main){
                    val channelsWithDateHeaders = createMessagesList(mentionsList)
                    mentionsListAdapter.submitList(channelsWithDateHeaders)
                }
            }
        }
    }

    private fun createMessagesList(mentions: List<Mention>): MutableList<BaseItem<*>> {
        // Wrap data in list items
        val mentionsItems = mentions.map {
            MentionList(it,requireActivity(),mentionsList)
        }

        val channelsWithDateHeaders = mutableListOf<BaseItem<*>>()
        // Loop through the channels list and add headers where we need them
        var currentHeader: String? = null

        mentionsItems.forEach{ c->
            /*val dateString = DateUtils.getRelativeTimeSpanString(convertStringDateToLong(c.data.timestamp.toString()),
                Calendar.getInstance().timeInMillis,
                DateUtils.DAY_IN_MILLIS)
            dateString.toString().let {
                if (it != currentHeader){
                    channelsWithDateHeaders.add(ChannelHeaderItem(it))
                    currentHeader = it
                }
            }*/
            channelsWithDateHeaders.add(c)
        }

        return channelsWithDateHeaders
    }
}