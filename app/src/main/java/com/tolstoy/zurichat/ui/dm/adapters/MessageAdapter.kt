package com.tolstoy.zurichat.ui.dm.adapters

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ItemMessageBinding
import com.tolstoy.zurichat.models.Message
import com.tolstoy.zurichat.util.FONT_KEY
import com.tolstoy.zurichat.util.changeVisibility
import com.tolstoy.zurichat.util.extractUrl
import com.tolstoy.zurichat.util.getWebsiteMetadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.format.DateTimeFormatter
import kotlin.random.Random

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created 02-Sep-21 at 12:00 AM
 *
 * @param currentUserId the current user of the app
 * @param messages the list of messages to display on the screen
 * @param isDm are the messages to be rendered gotten for dm or chat
 */
class MessageAdapter(
    private val context: Context,
    private val currentUserId: Int,
    private val messages: MutableList<Message> = mutableListOf(),
    private val isDm: Boolean = true):
    RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int): Unit = with(holder) {
        Timber.d("onBindViewHolder: " + messages[adapterPosition])
        val previousMessage = if(adapterPosition - 1 >= 0) messages[adapterPosition - 1] else null
        styleMessage(messages[adapterPosition], previousMessage, binding)
        // add on click listeners for each message
        binding.root.setOnClickListener {
            Timber.d("onClick: ap: $adapterPosition, p: $position")
            // this is just for testing purposes
            // switch the user id of the message whenever it is clicked
            // this aligns the message to the right or left of the screen
            Timber.d("onClick: previous message ${messages[adapterPosition]}")
            val message = messages[adapterPosition]
            messages[adapterPosition] =
                if (message.userId == currentUserId)
                    message.copy(userId = Random(100000).nextInt())
                else
                    message.copy(userId = currentUserId)
            Timber.d("onClick: new message ${messages[adapterPosition]}")

            notifyItemChanged(position)
        }
    }

    override fun getItemCount() = messages.size

    fun addMessage(message: Message){
        messages.add(message)
        notifyItemInserted(messages.lastIndex)    }

    private fun styleMessage(message: Message, previousMessage: Message?, messageBox: ItemMessageBinding){
        // load any hyperlink if there is one
        loadHyperlink(message, messageBox)

        // get chat font size
        val fontSizeText = PreferenceManager.getDefaultSharedPreferences(context).getString(FONT_KEY, "")
        var fontSizeSP = 0f
        when(fontSizeText) {
            "1" -> {
                fontSizeSP =  14f
            }
            "2" -> {
                fontSizeSP = 16f
            }
            "3" -> {
                fontSizeSP =  18f
            }
        }



        // place content in the message box
        messageBox.apply {
            textIMessageContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSizeSP)
            textIMessageSender.text = "John Felix Doe"
            textIMessageContent.text = message.content
            textIMessageTime.text = message.time.format(DateTimeFormatter.ofPattern("hh:mm a"))
        }

        if(isDm) {
            messageBox.apply {
                // hide the image and name of the sender if this message is being rendered in the dm
                changeVisibility(View.GONE, imageIMessageSender, textIMessageSender)
            }
        } else {
            // checks if the previous message and the current message were sent by the same user
            if(message.userId == previousMessage?.userId)
                // remove the name of the person that sent this message
                // if they were the same person that sent the previous message
                changeVisibility(View.GONE, messageBox.textIMessageSender)
        }

        if(currentUserId == message.userId){
            // checks if this user sent this message

            // remove the image of the user
            changeVisibility(View.GONE, messageBox.imageIMessageSender)

            alignMessage(messageBox, ALIGN_RIGHT.toFloat()) // align the message right
            // change the color of the message background
            messageBox.layoutIMessage.setBackgroundColor(
                ContextCompat.getColor(context, R.color.message_background_this_user))
        }else{
            // the message was sent by another user

            alignMessage(messageBox, ALIGN_LEFT.toFloat()) // align the message left
            // change the color of the message background
            messageBox.layoutIMessage.setBackgroundColor(
                ContextCompat.getColor(context, R.color.message_background_another_user))
        }
    }

    private fun loadHyperlink(message: Message, binding: ItemMessageBinding) = with(binding.linkPreviewIMessage){
        message.content.extractUrl()?.let { messageContent ->
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
                                Glide.with(context).load(image).into(imagePUrlImage)
                            }

                            changeVisibility(View.VISIBLE, textPUrlTitle)
                            textPUrlTitle.text = if(title.isNotBlank()){ title }else{ messageContent }
                        }
                    }
                }
            }
        }
    }

    /**
     * Aligns the message box depending on the bias, 1 means right, 0 means left
     *
     * @param bias the place to align the message to
     * @param message the message to align
     * */
    private fun alignMessage(message: ItemMessageBinding, bias: Float){
        // since the layout uses a constraint layout, we align the message using constraint set
        ConstraintSet().apply {
            clone(message.root)
            setHorizontalBias(message.layoutIMessage.id, bias)
            applyTo(message.root)
        }
    }

    inner class ViewHolder(val binding: ItemMessageBinding):
        RecyclerView.ViewHolder(binding.root)

    companion object {
        val TAG = MessageAdapter::class.simpleName

        const val ALIGN_LEFT = 0
        const val ALIGN_RIGHT = 1
    }
}