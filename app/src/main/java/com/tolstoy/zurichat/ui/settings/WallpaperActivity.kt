package com.tolstoy.zurichat.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.*
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.util.THEME_KEY
import com.tolstoy.zurichat.util.setUpApplicationTheme

private const val TITLE_TAG = "settingsActivityTitle"

class WallpaperActivity : AppCompatActivity(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallpaper_settings)

        val manageWallpaperContainer = findViewById<LinearLayout>(R.id.wallpaper_container)
        val divider = findViewById<View>(R.id.divider);

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(R.id.wallpaper_container, WallpaperFragment()).commit()
        } else {
            title = savedInstanceState.getCharSequence(TITLE_TAG)
        }
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount == 0) {
                setTitle(R.string.title_activity_settings)
                manageWallpaperContainer.visibility = View.VISIBLE
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
        supportFragmentManager.beginTransaction().replace(R.id.wallpaper_container, fragment).addToBackStack(null).commit()
        title = pref.title
        return true
    }

    class WallpaperFragment : PreferenceFragmentCompat(),SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.wallpaper_preferences, rootKey)

            val wallpaperContainer = activity?.findViewById<LinearLayout>(R.id.wallpaper_container)

            //make manage wallpaper container clickable
            wallpaperContainer?.setOnClickListener {
                startActivity(Intent(activity, ManageWallpaperActivity::class.java))
            }

            val barPref = findPreference<SeekBarPreference>("bar")
            barPref?.updatesContinuously = true

            PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this)

                }

            override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
                    val dimmer:View? = view?.findViewById<View>(R.id.dimmer_view)
                    if (key == "bar"){
                        val barIncr = sharedPreferences?.getInt("bar",50)?.toFloat()
                        val flt = barIncr?.div(100.0f)
                        if (flt != null) {
                            dimmer?.alpha=flt
                        }
                    }
                }
        }
    }





