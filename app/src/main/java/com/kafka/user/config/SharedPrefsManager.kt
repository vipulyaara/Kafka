package com.kafka.user.config

import android.content.Context
import android.content.SharedPreferences

/**
 * @author Vipul Kumar; dated 18/02/19.
 */
fun Context.appSharedPrefs(): SharedPreferences = getSharedPreferences("app-prefs", Context.MODE_PRIVATE)

const val PREF_KEY_THEME_MODE = "night mode"
