package com.tolstoy.zurichat.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ZuriSharedPreferences @Inject constructor(@ApplicationContext context: Context) {

    private var sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    public fun setString(key: String, value: String) {
        sharedPreferences.edit {
            putString(key, value)
            apply()
        }
    }

    public fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit {
            putBoolean(key, value)
            apply()
        }
    }

    public fun setInt(key: String, value: Int) {
        sharedPreferences.edit {
            putInt(key, value)
            apply()
        }
    }

    public fun getString(key: String, defaultString: String? = null) =
        sharedPreferences.getString(key, defaultString) ?: ""

    public fun getBoolean(key: String, defaultBoolean: Boolean = false) =
        sharedPreferences.getBoolean(key, defaultBoolean)

    public fun getInt(key: String, defaultInt: Int = 0) = sharedPreferences.getInt(key, defaultInt)


}
