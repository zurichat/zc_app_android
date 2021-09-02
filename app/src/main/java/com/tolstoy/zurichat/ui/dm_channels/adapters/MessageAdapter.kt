package com.tolstoy.zurichat.ui.dm_channels.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.ItemMessageBinding
import com.tolstoy.zurichat.model.Message
import com.tolstoy.zurichat.util.changeVisibility

/**
 * @author Jeffrey Orazulike [chukwudumebiorazulike@gmail.com]
 * Created 02-Sep-21 at 12:00 AM
 */
class MessageAdapter(
    private val context: Context,
    private val currentUserId: Int,
    private val messages: MutableList<Message> = mutableListOf(),
    private val isDm: Boolean = true): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int): Unit = with(holder) {
        styleMessage(messages[adapterPosition], messages[adapterPosition - 1], binding)
    }

    override fun getItemCount() = messages.size

    private fun styleMessage(message: Message, previousMessage: Message, messageBox: ItemMessageBinding){
        if(isDm) {
            messageBox.apply {
                // hide the image and name of the sender if this message is being rendered in the dm
                changeVisibility(View.GONE, imageIMessageSender, textIMessageSender)
            }
        } else {
            // checks if the previous message and the current message were sent by the same user
            if(message.userId == previousMessage.userId)
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
            messageBox.boxIMessage.setBackgroundColor(
                ContextCompat.getColor(context, R.color.message_background_this_user))
        }else{
            // the message was sent by another user

            alignMessage(messageBox, ALIGN_LEFT.toFloat()) // align the message left
            // change the color of the message background
            messageBox.boxIMessage.setBackgroundColor(
                ContextCompat.getColor(context, R.color.message_background_another_user))
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
        const val ALIGN_LEFT = 0
        const val ALIGN_RIGHT = 1
    }
}