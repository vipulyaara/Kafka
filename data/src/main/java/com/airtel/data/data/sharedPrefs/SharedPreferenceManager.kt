package com.airtel.data.data.sharedPrefs

import android.app.Application
import android.content.SharedPreferences

/**
 * Created by AkashGupta on 19/03/18.
 *
 */
abstract class SharedPreferenceManager constructor(context: Application) {
    lateinit var pref: SharedPreferences

    companion object {
        const val KEY_APP_VERSION_CODE = "key_app_version"
        const val KEY_PENDING_USER_UPDATE = "key_app_pending_app_update"
    }

    fun putInt(key: String, value: Int) {
        pref.edit().putInt(key, value).apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return pref.getInt(key, defValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        pref.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return pref.getBoolean(key, defValue)
    }

    fun putString(key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }

    fun getString(key: String, defValue: String): String {
        return pref.getString(key, defValue)
    }

    fun putLong(key: String, value: Long) {
        pref.edit().putLong(key, value).apply()
    }

    fun getLong(key: String, defValue: Long): Long {
        return pref.getLong(key, defValue)
    }

    fun clearPreferences() {
        pref.edit().clear().apply()
    }
}
