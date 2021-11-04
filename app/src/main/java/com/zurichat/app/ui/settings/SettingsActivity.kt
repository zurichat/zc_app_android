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
import com.zurichat.app.util.LocaleHelper
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

        user = intent.extras?.getParcelable(getString(R.string.user))!!
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
        //finish()
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

            user = requireActivity().intent.extras?.getParcelable(getString(R.string.user))!!


            val chatSettings = findPreference<Preference>(getString(R.string.chat_header))
            val securitySettings = findPreference<Preference>(getString(R.string.security_header))
            val storageSettings = findPreference<Preference>(getString(R.string.storage_header))
            val notificationSettings = findPreference<Preference>(getString(R.string.notification_header))
            val languageSettings = findPreference<Preference>(getString(R.string.languages))
            val logout = findPreference<Preference>(getString(R.string.logout_header))
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
                bundle.putParcelable(getString(R.string.user),user)
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
                logoutDialog.show(childFragmentManager, getString(R.string.logged_out))
                true
            }
        }

        private fun observeData() {
            userViewModel.logoutResponse.observe(viewLifecycleOwner, {
                when (it) {
                    is Result.Success -> {
                        ZuriSharePreference(requireContext()).setString(getString(R.string.current_org_id),"")
                        //Toast.makeText(context, "You have been successfully logged out", Toast.LENGTH_SHORT).show()
                        progressLoader.hide()
                        updateUser()
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
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

            val englishUK = findPreference<Preference>("en-uk")
            val englishUS = findPreference<Preference>("en-us")
            val french = findPreference<Preference>("fr")
            val chinese = findPreference<Preference>("zh")
            val arabic = findPreference<Preference>("ar")
            val deutsch = findPreference<Preference>("de")
            val spanish = findPreference<Preference>("es")
            val italian = findPreference<Preference>("it")
            val hebrew = findPreference<Preference>("iw")
            val portuguese = findPreference<Preference>("pt")

            englishUK!!.setOnPreferenceClickListener {
                LocaleHelper.setLocale(requireActivity(), "en").let {
                    requireActivity().recreate()
                }
                false
            }

            englishUS!!.setOnPreferenceClickListener {
                LocaleHelper.setLocale(requireActivity(), "en").let {
                    requireActivity().recreate()
                }
                false
            }

            french!!.setOnPreferenceClickListener {
                LocaleHelper.setLocale(requireActivity(), "fr").let {
                    requireActivity().recreate()
                }
                false
            }

            chinese!!.setOnPreferenceClickListener {
                LocaleHelper.setLocale(requireActivity(), "zh").let {
                    requireActivity().recreate()
                }
                false
            }

            arabic!!.setOnPreferenceClickListener {
                LocaleHelper.setLocale(requireActivity(), "ar").let {
                    requireActivity().recreate()
                }
                false
            }

            deutsch!!.setOnPreferenceClickListener {
                LocaleHelper.setLocale(requireActivity(), "de").let {
                    requireActivity().recreate()
                }
                false
            }

            spanish!!.setOnPreferenceClickListener {
                LocaleHelper.setLocale(requireActivity(), "es").let {
                    requireActivity().recreate()
                }
                false
            }

            italian!!.setOnPreferenceClickListener {
                LocaleHelper.setLocale(requireActivity(), "it").let {
                    requireActivity().recreate()
                }
                false
            }

            hebrew!!.setOnPreferenceClickListener {
                LocaleHelper.setLocale(requireActivity(), "iw").let {
                    requireActivity().recreate()
                }
                false
            }

            portuguese!!.setOnPreferenceClickListener {
                LocaleHelper.setLocale(requireActivity(), "pt").let {
                    requireActivity().recreate()
                }
                false
            }
            //TranslationSelectFragment()
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
            val channelTones = findPreference<SwitchPreference>(getString(R.string.channel_tones))
            val messageTone = findPreference<SwitchPreference>(getString(R.string.message_tone))
            val vibrate = findPreference<SwitchPreference>(getString(R.string.vibrate))
            val highPriority = findPreference<SwitchPreference>(getString(R.string.high_priority))


            channelTones?.setOnPreferenceChangeListener { preference, newValue ->
                if (channelTones.isChecked){
                    isChannelToneChecked = true
                    Toast.makeText(activity, getString(R.string.Channel_tones_off), Toast.LENGTH_SHORT).show()
                }else{
                    isChannelToneChecked = false
                    Toast.makeText(activity, getString(R.string.channel_tones_on), Toast.LENGTH_SHORT).show()
                }
                return@setOnPreferenceChangeListener true
            }
            messageTone?.setOnPreferenceChangeListener { preference, newValue ->
                if (messageTone.isChecked){
                    isMessageToneChecked = true
                    Toast.makeText(activity, getString(R.string.message_tones_off), Toast.LENGTH_SHORT).show()
                }else{
                    isMessageToneChecked = false
                    Toast.makeText(activity, getString(R.string.message_tones_on), Toast.LENGTH_SHORT).show()
                }
                return@setOnPreferenceChangeListener true
            }
            vibrate?.setOnPreferenceChangeListener { preference, newValue ->
                if (vibrate.isChecked){
                    isVibrateChecked = true
                    Toast.makeText(activity, getString(R.string.vibrate_off), Toast.LENGTH_SHORT).show()
                }else{
                    isVibrateChecked = false
                    Toast.makeText(activity, getString(R.string.vibrate_on), Toast.LENGTH_SHORT).show()
                    vibrateDevice(requireContext())
                }
                return@setOnPreferenceChangeListener true
            }
            highPriority?.setOnPreferenceChangeListener { preference, newValue ->
                if (highPriority.isChecked){
                    isHighPriorityChecked = true
                    Toast.makeText(activity, getString(R.string.high_priority_notification), Toast.LENGTH_LONG).show()
                }else{
                    isHighPriorityChecked = false
                    Toast.makeText(activity, getString(R.string.high_priority_notifications_on), Toast.LENGTH_LONG).show()
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
