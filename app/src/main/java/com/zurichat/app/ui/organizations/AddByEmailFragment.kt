package com.zurichat.app.ui.organizations

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.zurichat.app.R
import com.zurichat.app.databinding.FragmentAddByEmailBinding
import com.zurichat.app.util.isValidEmail
import com.zurichat.app.util.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddByEmailFragment : Fragment(R.layout.fragment_add_by_email) {

    private val binding by viewBinding(FragmentAddByEmailBinding::bind)
    private val addByEmailFragmentArgs: AddByEmailFragmentArgs by navArgs()
    private lateinit var organizationName: String
    private lateinit var organizationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        organizationName = addByEmailFragmentArgs.organizationName
        organizationId = addByEmailFragmentArgs.organizationId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSendRecipientEmail.setOnClickListener(fun(_: View) {
            val recipient = binding.recipientEmailEdit.text.toString()
            when {
                recipient.isEmpty() -> {
                    binding.recipientEmailEdit.error = "Input Recipient Email"
                }
                !recipient.isValidEmail ->{
                    binding.recipientEmailEdit.error = "Wrong Input, Only valid recipient email required"
                }
                else -> {
                    val intent = Intent(Intent.ACTION_SENDTO).apply{

                        putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "We look forward to your response 😊. Use this link below to join  👇\nhttps://api.zuri.chat/organizations/${organizationId}"
                        )
                        putExtra(
                            Intent.EXTRA_SUBJECT,
                            "You are invited to join $organizationName on Zuri Chat"
                        )
                        data = Uri.parse("mailto:")
                    }
                    try {
                        val shareIntent = Intent.createChooser(intent, "Send Email...")
                        startActivity(shareIntent)
                    }catch(e: Exception){
                        //if any thing goes wrong for example no email client application or any exception
                        //get and show exception message
                        Toast.makeText(this.context, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}