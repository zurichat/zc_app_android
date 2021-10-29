package com.zurichat.app.ui.settings

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import com.zurichat.app.R
import com.zurichat.app.databinding.ActivityChatBackupBinding
import com.zurichat.app.ui.settings.dialogs.BackUpGoogleDialogFragment
import com.zurichat.app.ui.settings.dialogs.BackUpOverDialogFragment

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
            backupOverDialog.show(supportFragmentManager, getString(R.string.backup_dialog))
        }

        binding.btnBackupGoogle.setOnClickListener {
            val backupGoogleDialog = BackUpGoogleDialogFragment(this)
            backupGoogleDialog.show(supportFragmentManager, getString(R.string.backup_google))
        }


    }

}