package com.zurichat.app.util

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.view.View
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat.getSystemService
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shreyaspatil.MaterialDialog.MaterialDialog
import com.zurichat.app.R
import com.zurichat.app.models.organization_model.UserOrganizationModel
import okhttp3.ResponseBody

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

fun handleErrorMessage(res: ResponseBody): String {
    var errorMessage = ""
    val gson = Gson()
    val type = object : TypeToken<UserOrganizationModel>() {}.type
    val errorResponse: UserOrganizationModel? = gson.fromJson(res.charStream(), type)
    if (errorResponse != null) {
        errorMessage = errorResponse.message
    }
    return errorMessage
}