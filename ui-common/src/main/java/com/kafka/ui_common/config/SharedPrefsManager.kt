package com.kafka.ui_common.config

import android.content.Context
import android.content.SharedPreferences

/**
 * @author Vipul Kumar; dated 18/02/19.
 */
fun Context.appSharedPrefs(): SharedPreferences = getSharedPreferences("app-prefs", Context.MODE_PRIVATE)

const val PREF_KEY_THEME_MODE = "night mode"

///private/var/folders/jr/pqsh_fp14zsc833m1yv04kqw0000gq/T/AppTranslocation/671B2C44-69EE-4E14-801D-501F0C4920C2/d/Android Studio 4.2 Preview 2.app/Contents/jre/jdk/Contents/Home
