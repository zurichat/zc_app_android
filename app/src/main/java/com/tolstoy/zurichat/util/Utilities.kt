package com.tolstoy.zurichat.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.preference.PreferenceManager
import com.tolstoy.zurichat.R

// Sets theme according to the string passed
fun setUpApplicationTheme(themeName : String){
    when(themeName){
        "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        "system_default" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}

//Gets the themeValue from sharedPref and sets the application theme from calling context accordingly
fun setUpApplicationTheme(context: Context){
    val themeName = PreferenceManager.getDefaultSharedPreferences(context).getString(
        THEME_KEY,context.getString(R.string.prefsThemesList_default))
    themeName?.let { setUpApplicationTheme(it) }
}


val String.isValidEmail: Boolean
    get() {
        val emailPattern = """[a-zA-Z0-9._-]+@[a-z]+\.+[a-z]+"""
        return matches(emailPattern.toRegex())
    }

// Vibrates the device for 100 milliseconds.
fun vibrateDevice(context: Context) {
    val vibrator = getSystemService(context, Vibrator::class.java)
    vibrator?.let {
        if (Build.VERSION.SDK_INT >= 26) {
            it.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            it.vibrate(100)
        }
    }
}