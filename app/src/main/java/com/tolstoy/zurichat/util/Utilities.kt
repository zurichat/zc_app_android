package com.tolstoy.zurichat.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
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