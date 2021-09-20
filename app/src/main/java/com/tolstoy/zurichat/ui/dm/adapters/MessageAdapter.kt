package com.tolstoy.zurichat.ui.dm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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
    RecyclerView.Adapter<MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            0 -> MessageViewHolder.TextMessage(ItemMessageBinding.inflate(inflater, parent, false))
            else -> MessageViewHolder.ImageMessage(ItemAttachmentImageBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
//        Timber.d("onBindViewHolder: " + messages[adapterPosition])
//        val previousMessage = if(adapterPosition - 1 >= 0) messages[adapterPosition - 1] else null
//        styleMessage(messages[adapterPosition], previousMessage, binding)
        // add on click listeners for each message
        holder.bind(messages[position])
    }

    override fun getItemViewType(position: Int) = messages[position].media?.size ?: 0

    override fun getItemCount() = messages.size

    fun addMessage(message: Message){
        messages.add(message)
        notifyItemInserted(messages.lastIndex)
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

    companion object {
        val TAG = MessageAdapter::class.simpleName

        const val ALIGN_LEFT = 0
        const val ALIGN_RIGHT = 1
    }
}