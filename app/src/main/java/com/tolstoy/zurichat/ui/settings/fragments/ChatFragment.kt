package com.tolstoy.zurichat.ui.settings.fragments

import android.content.Intent
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.settings.ChatBackupActivity

class ChatFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.chat_preferences, rootKey)

        findPreference<Preference>("chat_backup")?.setOnPreferenceClickListener {
            val intent = Intent(requireActivity(), ChatBackupActivity::class.java)
            startActivity(intent)

            true
        }
    }



}