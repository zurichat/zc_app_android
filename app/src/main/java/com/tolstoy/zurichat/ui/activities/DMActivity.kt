package com.tolstoy.zurichat.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tolstoy.zurichat.databinding.ActivityDmactivityBinding
import com.tolstoy.zurichat.model.Message
import com.tolstoy.zurichat.ui.dm_channels.adapters.MessageAdapter

class DMActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDmactivityBinding.inflate(layoutInflater)}
    private val adapter by lazy { MessageAdapter(this, 0) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupUI()
    }

    private fun setupUI() = with(binding){
        // setup recycler view
        chatView.let {
            it.layoutManager = LinearLayoutManager(this@DMActivity)
            it.adapter = adapter
        }
        fabVoiceNote.setOnClickListener {
            val message = edittextMessage.text.toString()
            if (message.isNotBlank()){
                adapter.addMessage(Message(0, message))
            }
            edittextMessage.setText("")
        }
    }
}