package com.tolstoy.zurichat.ui.settings.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.ui.settings.ChatBackupActivity
import com.tolstoy.zurichat.util.THEME_KEY
import com.tolstoy.zurichat.util.setUpApplicationTheme

class ChatFragment : PreferenceFragmentCompat() {

    private  var listPref : ListPreference? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val profileContainer = activity?.findViewById<ConstraintLayout>(R.id.profile_container)
        val divider = activity?.findViewById<View>(R.id.divider)
        profileContainer?.visibility = View.GONE
        divider?.visibility = View.GONE

        // Gets the listPreference object using its key
        listPref = preferenceManager.findPreference(THEME_KEY)

        /*
        checks for the value selected after making a choice from the listPreference and set up
        the application theme
         */
        listPref?.setOnPreferenceChangeListener { _, newValue ->
            setUpApplicationTheme(newValue as String)
            return@setOnPreferenceChangeListener true
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.chat_preferences, rootKey)

        findPreference<Preference>("chat_backup")?.setOnPreferenceClickListener {
            val intent = Intent(requireActivity(), ChatBackupActivity::class.java)
            startActivity(intent)

            true
        }
    }



}