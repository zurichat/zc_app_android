package com.tolstoy.zurichat.ui.settings

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import com.tolstoy.zurichat.databinding.ActivityChatBackupBinding
import com.tolstoy.zurichat.databinding.ActivityMainBinding
import com.tolstoy.zurichat.ui.settings.dialogs.BackUpOverDialogFragment

class ChatBackupActivity : AppCompatActivity() {

    private var _binding: ActivityChatBackupBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityChatBackupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        title = "Chat Backup"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {



        return super.onCreateView(name, context, attrs)
    }

    override fun onStart() {
        super.onStart()
        initialize()
    }

    private fun initialize() {

        binding.btnBackupOver.setOnClickListener {
            val backupOverDialog = BackUpOverDialogFragment(this)
            backupOverDialog.show(supportFragmentManager, "Back up Dialog")
        }

        binding.btnBackupGoogle.setOnClickListener {
            val backupGoogleDialog = BackUpOverDialogFragment(this)
            backupGoogleDialog.show(supportFragmentManager, "Back up Google")
        }


    }

}