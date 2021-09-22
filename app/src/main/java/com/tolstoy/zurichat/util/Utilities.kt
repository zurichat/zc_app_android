package com.tolstoy.zurichat.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.tolstoy.zurichat.R
import com.tolstoy.zurichat.models.User

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

fun Context.tempSaveUser(user: User?){
    val gson = Gson()
    val json = gson.toJson(user)
    ZuriSharedPreferences(this).setString("User",json)
}

fun Context.getTempUser():User?{
    val gson = Gson()
    val json: String = ZuriSharedPreferences(this).getString("User", "")
    return gson.fromJson(json, User::class.java)
}
