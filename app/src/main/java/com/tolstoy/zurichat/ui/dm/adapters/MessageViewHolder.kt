package com.tolstoy.zurichat.ui.dm.adapters

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ItemAttachmentImageBinding
import com.tolstoy.zurichat.databinding.ItemMessageBinding
import com.tolstoy.zurichat.models.Message
import com.tolstoy.zurichat.util.changeVisibility
import com.tolstoy.zurichat.util.extractUrl
import com.tolstoy.zurichat.util.getWebsiteMetadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

/**
 * @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
 * Created 18-Sep-21
 */
sealed class MessageViewHolder(binding: ViewBinding, val isDm: Boolean = true): RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(message: Message)

    class ImageMessage(val binding: ItemAttachmentImageBinding): MessageViewHolder(binding){
        override fun bind(message: Message) {
            if(message.media != null)
                Glide.with(binding.root.context.applicationContext)
                    .load(message.media.first()).into(binding.imageIAI)
        }
    }

    class TextMessage(val binding: ItemMessageBinding): MessageViewHolder(binding){

        override fun bind(message: Message) {
            binding.also {
                loadHyperlink(message)
                it.textIMessageSender.text = "John Felix Doe"
                it.textIMessageContent.text = message.message
//                textIMessageTime.text = message.time.format(DateTimeFormatter.ofPattern("hh:mm a"))

                if(isDm) {
                    // hide the image and name of the sender if this message is being rendered in the dm
                    changeVisibility(View.GONE, it.imageIMessageSender, it.textIMessageSender)
                } else {
                    // checks if the previous message and the current message were sent by the same user
//                    if(message.senderId == previousMessage?.userId)
                    // remove the name of the person that sent this message
                    // if they were the same person that sent the previous message
//                        changeVisibility(View.GONE, messageBox.textIMessageSender)
                }

//                if(currentUserId == message.userId){
//                    // checks if this user sent this message
//
//                    // remove the image of the user
//                    changeVisibility(View.GONE, messageBox.imageIMessageSender)
//
//                    alignMessage(messageBox, MessageAdapter.ALIGN_RIGHT.toFloat()) // align the message right
//                    // change the color of the message background
//                    messageBox.layoutIMessage.setBackgroundColor(
//                        ContextCompat.getColor(context, R.color.message_background_this_user))
//                }else{
//                    // the message was sent by another user
//
//                    alignMessage(messageBox, MessageAdapter.ALIGN_LEFT.toFloat()) // align the message left
//                    // change the color of the message background
//                    messageBox.layoutIMessage.setBackgroundColor(
//                        ContextCompat.getColor(context, R.color.message_background_another_user))
//                }
            }
        }

        private fun loadHyperlink(message: Message) = with(binding.linkPreviewIMessage){
            message.message.extractUrl()?.also { messageContent ->
                CoroutineScope(Dispatchers.IO).launch {
                    messageContent.getWebsiteMetadata()?.let { document ->
                        val metaTag = document.getElementsByTag("meta")
                        var image = ""
                        var title = ""
                        var description = ""
                        metaTag.forEach { element ->
                            when {
                                element.attr("property").equals("og:image") -> {
                                    image = element.attr("content")
                                }
                                element.attr("name").equals("title") ||
                                        element.attr("name").equals("og:title")-> {
                                    title = element.attr("content")
                                }
                                element.attr("name").equals("description") ||
                                        element.attr("name").equals("og:description") -> {
                                    description = element.attr("content")
                                }
                            }
                        }
                        CoroutineScope(Dispatchers.Main).launch {
                            if(description.isNotBlank()){
                                changeVisibility(View.VISIBLE, textPUrlDescription)
                                textPUrlDescription.text = description

                                if(image.isNotBlank()){
                                    changeVisibility(View.VISIBLE, imagePUrlImage)
                                    Glide.with(root.context.applicationContext).load(image).into(imagePUrlImage)
                                }

                                changeVisibility(View.VISIBLE, textPUrlTitle)
                                textPUrlTitle.text = if(title.isNotBlank()){ title }else{ messageContent }
                            }
                        }
                    }
                }
            }
        }

    }
}