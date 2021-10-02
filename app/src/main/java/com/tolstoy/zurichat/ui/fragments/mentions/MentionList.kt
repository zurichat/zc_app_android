package com.tolstoy.zurichat.ui.fragments.mentions

import android.app.Activity
import android.view.View
import androidx.core.content.ContextCompat
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ChannelsListItemBinding
import com.tolstoy.zurichat.databinding.MentionsListItemsBinding
import com.tolstoy.zurichat.models.ChannelModel
import com.tolstoy.zurichat.ui.add_channel.BaseItem
import java.text.SimpleDateFormat
import java.util.*

data class MentionList(val mention: Mention, val context: Activity, val mentionList: ArrayList<Mention>) :
    BaseItem<MentionsListItemsBinding> {

    override val layoutId = R.layout.mentions_list_items
    override val uniqueId: String = mention.messageID

    override fun initializeViewBinding(view: View) = MentionsListItemsBinding.bind(view)

    override fun bind(binding: MentionsListItemsBinding, itemClickCallback: ((BaseItem<MentionsListItemsBinding>) -> Unit)?) {
        val s = SimpleDateFormat("hh:mm aa")
        val time = s.format(convertStringDateToLong(mention.timeStamp))

        binding.root.setOnClickListener {
            itemClickCallback?.invoke(this)
        }
        binding.mentionUserName.text = mention.senderID
        binding.mentionsTextDesc.text = mention.content
        binding.timeMentionedTxtView.text = time
    }

    private fun convertStringDateToLong(date: String): Date {
        val s = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        s.timeZone = TimeZone.getTimeZone("UTC")
        return s.parse(date)
    }
}