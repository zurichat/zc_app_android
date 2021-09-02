package com.tolstoy.zurichat.ui.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tolstoy.zurichat.R

class DMActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dmactivity)
        val dmEditText = findViewById<EditText>(R.id.edittext_message)
        val sendVoiceNote = findViewById<FloatingActionButton>(R.id.fab_voiceNote)
        val sendMessage = findViewById<FloatingActionButton>(R.id.fab_send_text)
        var text: String? = null

        // This listener checks whether the edittext has content that is not spaces
        // it then proceeds to update the floating action button respectively
        dmEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text = s.toString()
                val currentMessage = dmEditText.text.toString()
                if (currentMessage.isEmpty()){
                    sendMessage.isEnabled=false
                    sendVoiceNote.isEnabled=true
                }else{
                    sendMessage.isEnabled=true
                    sendVoiceNote.isEnabled=false
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    fun sendText(message: String) {

    }

}