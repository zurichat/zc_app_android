package com.tolstoy.zurichat.ui.fragments.channel_chat

import android.app.Activity
import android.view.View
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ChannelIncomingMessageModelBinding
import com.tolstoy.zurichat.models.User
import com.tolstoy.zurichat.ui.add_channel.BaseItem
import com.tolstoy.zurichat.ui.fragments.model.Data
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.util.*

data class ChannelListItem (val data: Data,val user:User, val context: Activity,val uiScope: CoroutineScope) : BaseItem<ChannelIncomingMessageModelBinding> {
    override val layoutId = R.layout.channel_incoming_message_model
    override val uniqueId: String = data._id.toString()

    override fun initializeViewBinding(view: View) = ChannelIncomingMessageModelBinding.bind(view)

    override fun bind(binding: ChannelIncomingMessageModelBinding, itemClickCallback: ((BaseItem<ChannelIncomingMessageModelBinding>) -> Unit)?) {
        val s = SimpleDateFormat("hh:mm aa")
        val time = s.format(convertStringDateToLong(data.timestamp.toString()))
        binding.root.setOnClickListener {
            itemClickCallback?.invoke(this)
        }
        if (data.user_id.toString() == user.id){
            binding.bubble1.visibility = View.VISIBLE
            binding.bubble.visibility = View.GONE
            binding.imageParent.visibility = View.GONE

            binding.messageText1.text = data.content
            binding.messageTime1.text = time
        }else{
            binding.bubble1.visibility = View.GONE
            binding.bubble.visibility = View.VISIBLE
            binding.imageParent.visibility = View.VISIBLE

            binding.messageText.text = data.content
            binding.messageTime.text = time
            binding.messageAuthor.text = data.user_id

            /*uiScope.launch(Dispatchers.IO){
                try {
                    val user = RetrofitClientInstance.retrofitInstanceForUser?.create(JoinNewChannel::class.java)?.getUserInfo(data.user_id.toString(),user.token)
                    user?.let {
                        uiScope.launch(Dispatchers.Main){
                            if (!(it.data.first_name.isEmpty() && it.data.last_name.isEmpty())){
                                binding.messageAuthor.text = it.data.first_name.plus(" "+it.data.last_name)
                            }else if (it.data.first_name.isNotEmpty()){
                                binding.messageAuthor.text = it.data.first_name
                            }else if (it.data.last_name.isNotEmpty()){
                                binding.messageAuthor.text = it.data.last_name
                            }else{
                                binding.messageAuthor.text = it.data.email
                            }
                        }
                    }
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }*/
        }

    }

    private fun convertStringDateToLong(date: String): Date {
        val s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        s.timeZone = TimeZone.getTimeZone("UTC")
        return s.parse(date)
    }

}