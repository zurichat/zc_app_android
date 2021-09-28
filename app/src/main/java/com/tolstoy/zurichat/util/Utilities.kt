package com.tolstoy.zurichat.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.shreyaspatil.MaterialDialog.MaterialDialog
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

fun createProgressDialog(context: Context) : ProgressDialog{
    return ProgressDialog(context)
}

fun generateMaterialDialog(
    context: Activity, title: String, message: String
    , positiveBtnTitle: String,
    positiveAction: (() -> Unit)?
){
    MaterialDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveBtnTitle) { dialogInterface, _ ->
            dialogInterface.dismiss()
            positiveAction?.invoke()
        }.setCancelable(true)
        .build()
        .show()
}

fun View.showSnackBar(message: String){
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(resources.getColor(R.color.background_color))
        .show()
}



val String.isValidEmail: Boolean
    get() {
        val emailPattern = """[a-zA-Z0-9._-]+@[a-z]+\.+[a-z]+"""
        return matches(emailPattern.toRegex())
    }