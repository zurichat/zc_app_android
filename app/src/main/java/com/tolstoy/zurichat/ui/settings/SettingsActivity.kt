package com.tolstoy.zurichat.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.tolstoy.zurichat.R

private const val TITLE_TAG = "settingsActivityTitle"

class SettingsActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        val profileContainer = findViewById<ConstraintLayout>(R.id.profile_container)
        val manageStorageContainer = findViewById<ConstraintLayout>(R.id.manage_storage_container)
        val networkUsageContainer = findViewById<ConstraintLayout>(R.id.network_usage_container)
        val divider = findViewById<View>(R.id.divider);

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment()).commit()
        } else {
            title = savedInstanceState.getCharSequence(TITLE_TAG)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                setTitle(R.string.title_activity_settings)
                profileContainer.visibility = View.VISIBLE
                manageStorageContainer.visibility = View.GONE
                networkUsageContainer.visibility = View.GONE
                divider.visibility = View.VISIBLE
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save current activity title so we can set it again after a configuration change
        outState.putCharSequence(TITLE_TAG, title)
    }

    override fun onSupportNavigateUp(): Boolean {
        if (supportFragmentManager.popBackStackImmediate()) {
            return true
        }
        return super.onSupportNavigateUp()
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment = supportFragmentManager.fragmentFactory.instantiate(classLoader, pref.fragment).apply {
            arguments = args
            setTargetFragment(caller, 0)
        }
        // Replace the existing Fragment with the new Fragment
        supportFragmentManager.beginTransaction().replace(R.id.settings, fragment).addToBackStack(null).commit()
        title = pref.title
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.settings_preferences, rootKey)

            val chatSettings = findPreference<Preference>("chat_header")
            val securitySettings = findPreference<Preference>("security_header")
            val storageSettings = findPreference<Preference>("storage_header")
            val notificationSettings = findPreference<Preference>("notification_header")

            val profileContainer = activity?.findViewById<ConstraintLayout>(R.id.profile_container)
            val manageStorageContainer = activity?.findViewById<ConstraintLayout>(R.id.manage_storage_container)
            val networkUsageContainer = activity?.findViewById<ConstraintLayout>(R.id.network_usage_container)
            val divider = activity?.findViewById<View>(R.id.divider);

            chatSettings!!.setOnPreferenceClickListener {
                if (profileContainer != null) {
                    profileContainer.visibility = View.GONE
                }
                if (manageStorageContainer != null) {
                    manageStorageContainer.visibility = View.GONE
                }
                if (networkUsageContainer != null) {
                    networkUsageContainer.visibility = View.GONE
                }
                if (divider!=null){
                    divider.visibility = View.GONE
                }
                false
            }

            securitySettings!!.setOnPreferenceClickListener {
                if (profileContainer != null) {
                    profileContainer.visibility = View.GONE
                }
                if (manageStorageContainer != null) {
                    manageStorageContainer.visibility = View.GONE
                }
                if (networkUsageContainer != null) {
                    networkUsageContainer.visibility = View.GONE
                }
                if (divider!=null){
                    divider.visibility = View.GONE
                }
                false
            }

            storageSettings!!.setOnPreferenceClickListener {
                if (profileContainer != null) {
                    profileContainer.visibility = View.GONE
                }
                if (manageStorageContainer != null) {
                    manageStorageContainer.visibility = View.VISIBLE
                }
                if (networkUsageContainer != null) {
                    networkUsageContainer.visibility = View.VISIBLE
                }
                false
            }

            notificationSettings!!.setOnPreferenceClickListener {
                if (profileContainer != null) {
                    profileContainer.visibility = View.GONE
                }
                if (manageStorageContainer != null) {
                    manageStorageContainer.visibility = View.GONE
                }
                if (networkUsageContainer != null) {
                    networkUsageContainer.visibility = View.GONE
                }
                if (divider!=null){
                    divider.visibility = View.GONE
                }
                false
            }
        }
    }

    class ChatFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.chat_preferences, rootKey)
        }
    }

    class PrivacyAndSecurityFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.privacy_and_security_preferences, rootKey)
        }
    }

    class StorageAndDataFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.storage_and_data_preferences, rootKey)
        }
    }

    class NotificationAndSounds : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.notifications_and_sound, rootKey)
        }
    }
}
