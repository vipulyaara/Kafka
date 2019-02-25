package com.airtel.kafkapp.config

import android.content.Context

/**
 * @author Vipul Kumar; dated 18/02/19.
 */
fun Context.appSharedPrefs() = getSharedPreferences("app-prefs", Context.MODE_PRIVATE)

const val PREF_KEY_THEME_MODE = "night mode"
