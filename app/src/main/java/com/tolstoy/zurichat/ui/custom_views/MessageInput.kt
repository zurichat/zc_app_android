package com.tolstoy.zurichat.ui.custom_views

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.databinding.CustomViewMessageInputBinding
import com.tolstoy.zurichat.models.Message

/**
* @author Jeffrey Orazulike [https://github.com/jeffreyorazulike]
* Created 12-Sep-21
 *
 * A compound component that acts as a widget for typing in messages
 *
 * Make sure a user id is passed into the message input view after initialization
 * before the submit fab is clicked if not a not initialized exception will be thrown
*/
class MessageInput @JvmOverloads
constructor(context: Context, attributeSet: AttributeSet? = null,
            defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    ConstraintLayout(context, attributeSet, defStyleAttr, defStyleRes) {

    // items used by view
    private val sendDrawable by lazy {
        ResourcesCompat.getDrawable(context.resources, R.drawable.ic_send, null)
    }
    private val speakDrawable by lazy {
        ResourcesCompat.getDrawable(context.resources, android.R.drawable.ic_btn_speak_now, null)
    }
    private var currentFABDrawable: Drawable?

    // listeners
    private val fabClickListeners = mutableListOf<(Message?, FAB_STATE) -> Unit>()
    private val emojiClickListeners = mutableListOf<() -> Unit>()
    private val attachmentClickListeners = mutableListOf<() -> Unit>()
    private val imageClickListeners = mutableListOf<() -> Unit>()

    private var binding: CustomViewMessageInputBinding
    // holds the current message being contained by this view
    private var message: Message? = null
    // holds the user id for the message being sent
    private lateinit var userId: String

    init{
        // inflate the message input view
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val root = inflater.inflate(R.layout.custom_view_message_input, this)
        // bind the view to the view holder
        binding = CustomViewMessageInputBinding.bind(root)
        currentFABDrawable = speakDrawable

        setupView()
        setupListeners()
    }

    private fun setupView() = binding.apply {
        fabMIRecordAudio.setImageDrawable(currentFABDrawable)
    }

    fun addFabClickListeners(listener: (Message?, FAB_STATE) -> Unit) = fabClickListeners.add(listener)
    fun addEmojiClickListeners(listener: () -> Unit) = emojiClickListeners.add(listener)
    fun addAttachmentClickListeners(listener: () -> Unit) = attachmentClickListeners.add(listener)
    fun addImageClickListeners(listener: () -> Unit) = imageClickListeners.add(listener)

    private fun setupListeners() = binding.also{
        it.textinputMIMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                // checks if the the user types in a something
                // if they do change the fab drawable
                if(p0?.isNotBlank() == true && currentFABDrawable != sendDrawable){
                    currentFABDrawable = sendDrawable
                    it.fabMIRecordAudio.setImageDrawable(currentFABDrawable)
                }else if(currentFABDrawable != speakDrawable) {
                    currentFABDrawable = speakDrawable
                    it.fabMIRecordAudio.setImageDrawable(currentFABDrawable)
                }
            }
        })
        it.fabMIRecordAudio.setOnClickListener { _ ->
            val message = Message(userId.toInt(), it.textinputMIMessage.text.toString())
            if(currentFABDrawable == speakDrawable) fabClickListeners.forEach { listener ->
                listener(null, FAB_STATE.SPEAK)
            } else fabClickListeners.forEach { listener ->
                listener(message, FAB_STATE.SEND)
            }
        }
        it.imageMIAttachFile.setOnClickListener {
            attachmentClickListeners.forEach { it() }
        }
        it.imageMIAttachImage.setOnClickListener {
            imageClickListeners.forEach { it() }
        }
    }

    companion object {
        // describes what state the floating action button was in when clicked
        enum class FAB_STATE {
            SPEAK, SEND
        }
    }
}