package com.zurichat.app.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.preference.*
import com.zurichat.app.R
import com.zurichat.app.models.LogoutBody
import com.zurichat.app.models.User
import com.zurichat.app.ui.activities.ProfileActivity
import com.zurichat.app.ui.fragments.switch_account.UserViewModel
import com.zurichat.app.ui.login.LoginActivity
import com.zurichat.app.ui.login.LoginViewModel
import com.zurichat.app.ui.organizations.utils.ZuriSharePreference
import com.zurichat.app.ui.settings.dialogs.LogOutDialogFragment
import com.zurichat.app.util.ProgressLoader
import com.zurichat.app.util.Result
import com.zurichat.app.util.vibrateDevice
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TITLE_TAG = "settingsActivityTitle"
typealias Callback = () -> Unit

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(), PreferenceFragmentCompat.OnPreferenceStartFragmentCallback, SharedPreferences.OnSharedPreferenceChangeListener {

    var soundPool: SoundPool? = null
    private lateinit var user : User
    private lateinit var profileImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        initializePool()

        val profileContainer = findViewById<ConstraintLayout>(R.id.profile_container)
        val manageStorageContainer = findViewById<ConstraintLayout>(R.id.manage_storage_container)
        val networkUsageContainer = findViewById<ConstraintLayout>(R.id.network_usage_container)
        val divider = findViewById<View>(R.id.divider)
        val nameTxt = findViewById<TextView>(R.id.name)
        profileImage = findViewById(R.id.profile_image)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.settings, SettingsFragment())
                .commit()
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

        user = intent.extras?.getParcelable("USER")!!
        if (!(user.first_name.isEmpty() && user.last_name.isEmpty())){
            nameTxt.text = user.first_name.plus(" "+user.last_name)
        }else if (user.first_name.isNotEmpty()){
              nameTxt.text = user.first_name
        }else if (user.last_name.isNotEmpty()){
             nameTxt.text = user.last_name
        }else{
             nameTxt.text = user.email
        }
    }

    override fun onResume() {
        super.onResume()

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val mImageUri = preferences.getString("image", null)
        if(mImageUri != null){
            profileImage.setImageURI(Uri.parse(mImageUri))
        }
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
        super.onBackPressed()
        return false
    }

    override fun onPreferenceStartFragment(caller: PreferenceFragmentCompat, pref: Preference): Boolean {
        // Instantiate the new Fragment
        val args = pref.extras
        val fragment =
            supportFragmentManager.fragmentFactory.instantiate(classLoader, pref.fragment).apply {
                arguments = args
                setTargetFragment(caller, 0)
            }
        // Replace the existing Fragment with the new Fragment
        supportFragmentManager.beginTransaction().replace(R.id.settings, fragment)
            .addToBackStack(null).commit()
        title = pref.title
        return true
    }

    @AndroidEntryPoint
    class SettingsFragment : PreferenceFragmentCompat() {
        @Inject
        lateinit var progressLoader: ProgressLoader
        private val userViewModel: LoginViewModel by viewModels()
        private val ViewModel by viewModels<UserViewModel>()
        private lateinit var user : User

        override fun onPreferenceTreeClick(preference: Preference?): Boolean {
            return super.onPreferenceTreeClick(preference)
        }
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            observeData()
        }
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.settings_preferences, rootKey)

            user = requireActivity().intent.extras?.getParcelable("USER")!!


            val chatSettings = findPreference<Preference>("chat_header")
            val securitySettings = findPreference<Preference>("security_header")
            val storageSettings = findPreference<Preference>("storage_header")
            val notificationSettings = findPreference<Preference>("notification_header")
            val languageSettings = findPreference<Preference>(getString(R.string.languages))
            val logout = findPreference<Preference>("logout_header")
            val profileContainer = activity?.findViewById<ConstraintLayout>(R.id.profile_container)


            val manageStorageContainer = activity?.findViewById<ConstraintLayout>(R.id.manage_storage_container)
            val networkUsageContainer = activity?.findViewById<ConstraintLayout>(R.id.network_usage_container)
            val divider = activity?.findViewById<View>(R.id.divider)

            //make manage storage container clickable
            manageStorageContainer?.setOnClickListener {
                startActivity(Intent(activity, ManageStorageActivity::class.java))
            }

            //make profile container clickable
            profileContainer?.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("USER",user)
                val intent = Intent(requireContext(), ProfileActivity::class.java)
                intent.putExtras(bundle)
                startActivity(intent)
            }

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
                if (divider != null) {
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
                if (divider != null) {
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
                if (divider != null) {
                    divider.visibility = View.GONE
                }
                false
            }
            languageSettings!!.setOnPreferenceClickListener {
                if (profileContainer != null) {
                    profileContainer.visibility = View.GONE
                }
                if (manageStorageContainer != null) {
                    manageStorageContainer.visibility = View.GONE
                }
                if (networkUsageContainer != null) {
                    networkUsageContainer.visibility = View.GONE
                }
                if (divider != null) {
                    divider.visibility = View.GONE
                }
                false
            }
            logout!!.setOnPreferenceClickListener {
                //logout()
                val callback: Callback = { logoutUser() }
                val logoutDialog = LogOutDialogFragment(callback)
                logoutDialog.show(childFragmentManager, "LOG_OUT")
                true
            }
        }

        private fun observeData() {
            userViewModel.logoutResponse.observe(viewLifecycleOwner, {
                when (it) {
                    is Result.Success -> {
                        ZuriSharePreference(requireContext()).setString("Current Organization ID","")
                        //Toast.makeText(context, "You have been successfully logged out", Toast.LENGTH_SHORT).show()
                        progressLoader.hide()
                        updateUser()
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        progressLoader.hide()
                    }
                    is Result.Loading -> {
                        progressLoader.show(getString(R.string.final_logout))
                        //Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        private fun updateUser(){
            val user = user?.copy(currentUser = false)
            ViewModel.updateUser(user!!)
        }
        private fun logoutUser() {
            val logoutBody = LogoutBody(email = user.email)
            userViewModel.logout(logoutBody)
            userViewModel.clearUserAuthState()
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

    class LanguagesFragment : PreferenceFragmentCompat(){
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.language_preferences, rootKey)
            TranslationSelectFragment()
        }

    }

    class TranslationSelectFragment: PreferenceFragmentCompat(){
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            val langIntent: Intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(langIntent)
        }

    }

    class NotificationAndSounds : PreferenceFragmentCompat() {
        var isChannelToneChecked: Boolean = false
        var isMessageToneChecked: Boolean = false
        var isVibrateChecked: Boolean = false
        var isHighPriorityChecked: Boolean = false

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

            setPreferencesFromResource(R.xml.notifications_and_sound, rootKey)
            val channelTones = findPreference<SwitchPreference>("channel_tones")
            val messageTone = findPreference<SwitchPreference>("message_tone")
            val vibrate = findPreference<SwitchPreference>("vibrate")
            val highPriority = findPreference<SwitchPreference>("high_priority")


            channelTones?.setOnPreferenceChangeListener { preference, newValue ->
                if (channelTones.isChecked){
                    isChannelToneChecked = true
                    Toast.makeText(activity, "Channel tones off", Toast.LENGTH_SHORT).show()
                }else{
                    isChannelToneChecked = false
                    Toast.makeText(activity, "Channel tones on", Toast.LENGTH_SHORT).show()
                }
                return@setOnPreferenceChangeListener true
            }
            messageTone?.setOnPreferenceChangeListener { preference, newValue ->
                if (messageTone.isChecked){
                    isMessageToneChecked = true
                    Toast.makeText(activity, "Message tones off", Toast.LENGTH_SHORT).show()
                }else{
                    isMessageToneChecked = false
                    Toast.makeText(activity, "Message tones on", Toast.LENGTH_SHORT).show()
                }
                return@setOnPreferenceChangeListener true
            }
            vibrate?.setOnPreferenceChangeListener { preference, newValue ->
                if (vibrate.isChecked){
                    isVibrateChecked = true
                    Toast.makeText(activity, "Vibrate off", Toast.LENGTH_SHORT).show()
                }else{
                    isVibrateChecked = false
                    Toast.makeText(activity, "Vibrate on", Toast.LENGTH_SHORT).show()
                    vibrateDevice(requireContext())
                }
                return@setOnPreferenceChangeListener true
            }
            highPriority?.setOnPreferenceChangeListener { preference, newValue ->
                if (highPriority.isChecked){
                    isHighPriorityChecked = true
                    Toast.makeText(activity, "High priority Notifications off", Toast.LENGTH_LONG).show()
                }else{
                    isHighPriorityChecked = false
                    Toast.makeText(activity, "High priority Notifications on", Toast.LENGTH_LONG).show()
                }
                return@setOnPreferenceChangeListener true
            }


        }
    }

    class ChannelsPrefFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.channels_pref, rootKey)
        }
    }

    class LiveLocationPrefFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.live_location_pref, rootKey)
        }
    }

    class BlockedContactsPrefFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.blocked_contacts_pref, rootKey)
        }
    }

    class FingerPrintPrefFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.fingerprint_pref, rootKey)
        }
    }

    //    listening to changes on the sharedPreference
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
     val  sound:Int = soundPool?.load(this,R.raw.swit,1)!!
            if (key.equals("channel_tones")){
                soundPool?.autoPause()
                soundPool?.play(sound, 1F, 1F,0,0, 1F)
            }
        Toast.makeText(this, key, Toast.LENGTH_LONG).show()
    }

    fun initializePool() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val audioAttributes: AudioAttributes = AudioAttributes.Builder().build()
            soundPool =
                SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build()
        }else{
            soundPool = SoundPool(6,AudioManager.STREAM_NOTIFICATION,0)
        }


    }

}
