package com.zurichat.app.ui.dm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zurichat.app.R
import com.zurichat.app.databinding.ItemMessageBinding
import com.zurichat.app.models.Message
import com.zurichat.app.util.FONT_KEY
import com.zurichat.app.util.changeVisibility
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created 02-Sep-21 at 12:00 AM
 *
 * @param currentUserId the current user of the app
 * @param messages the list of messages to display on the screen
 * @param isDm are the messages to be rendered gotten for dm or channels
 */
class MessageAdapter(
    private val context: Context,
    private val currentUserId: String,
    val messages: MutableList<Message> = mutableListOf(),
    private val isDm: Boolean = true
) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMessageBinding
            .inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // get chat font size
        val fontSizeText =
            PreferenceManager.getDefaultSharedPreferences(context).getString(FONT_KEY, "")
        var fontSizeSP = 0f
        when (fontSizeText) {
            "1" -> {
                fontSizeSP = 14f
            }
            "2" -> {
                fontSizeSP = 16f
            }
            "3" -> {
                fontSizeSP = 18f
            }
        }

        messages[position].fontSize = fontSizeSP
        holder.bind(if (position - 1 >= 0) messages[position - 1] else null, messages[position])
    }

    override fun getItemCount() = messages.size

    fun addMessage(message: Message, position: Int = -1): Int {
        var _position = position
        if(position >= 0 && position < messages.size) messages[position] = message
        else {
            messages.add(message)
            _position = messages.lastIndex
        }
        notifyItemInserted(_position)
        return _position
    }

    inner class ViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(previousMessage: Message?, message: Message) = with(binding) {
            determineMessageItemToDisplay(message)
            if(currentUserId != message.senderId) alignMessage(0)
            if (!isDm && currentUserId != message.senderId && message.senderId != previousMessage?.senderId ) {
                displayForChannel()
            }
            // make sure the width of the item doesn't pass 80% of the recycler views width
            root.layoutParams = root.layoutParams.also { params ->
                recyclerView?.let{ rv ->
                    val percentWidth = 0.8 * rv.measuredWidth
                    if(params.width > percentWidth) params.width = percentWidth.toInt()
                }
            }
        }

        /**
         * A single message item (i.e either text, image, audio or document) can be shown at once.
         * This method determines which one to display
         *
         * @param message the message to display
         * */
        private fun determineMessageItemToDisplay(message: Message) = with(binding){
            if (!message.attachments.isNullOrEmpty()){
                includeMessageImage.also {
                    changeVisibility(View.VISIBLE, it.root)
                    Glide.with(context.applicationContext).load(message.attachments.first()).into(it.imageMI)
                }
            } else {
                // display the messages text
                includeMessageText.also{
//                    changeVisibility(View.VISIBLE, it.root)
//                    it.textMTContent.setTextSize(
//                        TypedValue.COMPLEX_UNIT_SP, message.fontSize ?: 0f
//                    )
                    it.textMTContent.text = message.message
                    it.includeMTSent.textIMessageTime.text = LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("hh:mm a"))
                }
            }
        }

        /**
         * Aligns the message box and changes the color of the message box
         * depending on the value of the bias
         *
         * 0 -> left and color for another user
         * 1 -> right and color for this user
         *
         * @param bias the place to align the message to
         * */
        private fun alignMessage(bias: Int) = with(binding) {
            // since the layout uses a constraint layout, we align the message using constraint set
            ConstraintSet().apply {
                clone(root)
                setHorizontalBias(layoutMessage.id, bias.toFloat())
                applyTo(root)
            }
            when(bias){
                0 -> layoutMessage.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.message_background_another_user)
                )
                1 -> layoutMessage.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.message_background_this_user)
                )
            }
        }

        /**
         * Styles a message item for channel chat
         *
         * This method should only be called when rendering a message for a different user in the
         * channel chat because the message sent in the dm and the one sent by the current user in
         * the channel looks the same
         *
         * Also, this method should be called if and only if the previous message was not from the
         * same person
         * */
        private fun displayForChannel() = with(binding){
            // show the image and name of the sender of the message
            changeVisibility(View.VISIBLE, imageMessageSender, textMessageSender)
            // TODO: Load the name and image of the sender from the user's repository and display it
        }

        /**
         * Loads a hyperlink from the selected message into the message bubble
         * */
//        private fun loadHyperlink(message: Message) = with(binding.includeIMessagePlug) {
//            fun Element.getContent(key: String, vararg values: String): String? {
//                val value = values.firstOrNull { attr(key).equals(it, true) }
//                return if(value != null) attr("content") else null
//            }
//
//            val url = message.message.extractUrl() ?: return@with
//
//            CoroutineScope(Dispatchers.IO).launch {
//                val document = url.getWebsiteMetadata() ?: return@launch
//                val metaTag = document.getElementsByTag("meta")
//                var image = ""
//                var title = ""
//                var description = ""
//                metaTag.forEach { element ->
//                    element.getContent("property", "og:image")?.let {
//                        image = it
//                    }
//                    element.getContent("name", "og:image", "title")?.let {
//                        title = it
//                    }
//                    element.getContent("name", "og:description", "description")?.let {
//                        description = it
//                    }
//                }
//                CoroutineScope(Dispatchers.Main).launch {
//                    if (description.isNotBlank()) {
//                        changeVisibility(View.VISIBLE, binding.includeIMessagePlug.layoutUrlPreview.rootView)
//                        textPUrlDescription.text = description
//
//                        if (image.isNotBlank()) {
//                            com.bumptech.glide.Glide.with(root.context.applicationContext)
//                                .load(image).into(imagePUrlImage)
//                        }
//
//                        textPUrlTitle.text = if (title.isNotBlank()) {
//                            title
//                        } else url.substringBeforeLast('.')
//                    }
//                }
//            }
//        }
    }

    companion object {
        val TAG = MessageAdapter::class.simpleName
    }

}