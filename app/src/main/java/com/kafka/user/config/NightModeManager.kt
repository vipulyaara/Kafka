package com.kafka.user.config

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import com.kafka.user.R

/**
 * @author Vipul Kumar; dated 18/02/19.
 */
object NightModeManager {
    fun enableNightMode(activity: Activity?) {
        activity?.let {
            setCurrentNightMode(activity, AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            activity.window?.setWindowAnimations(R.style.Theme_Coyote_ThemeTransitionAnimation)
            activity.recreate()
        }
    }

    fun disableNightMode(activity: Activity?) {
        activity?.let {
            setCurrentNightMode(activity, AppCompatDelegate.MODE_NIGHT_NO)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            activity.window.setWindowAnimations(R.style.Theme_Coyote_ThemeTransitionAnimation)
            activity.recreate()
        }
    }

    fun toggleNightMode(activity: Activity?) {
        activity?.let {
            val nightMode = getCurrentMode(activity)
            when (nightMode) {
                AppCompatDelegate.MODE_NIGHT_YES -> disableNightMode(activity)
                else -> enableNightMode(activity)
            }
        }
    }

    fun setNightModeFromSharedPrefs(context: Context) {
        AppCompatDelegate
            .setDefaultNightMode(getCurrentMode(context))
    }

    fun setCurrentNightMode(context: Context, value: Int) {
        context.appSharedPrefs().edit { putInt(PREF_KEY_THEME_MODE, value) }
    }

    fun getCurrentMode(context: Context): Int {
        return context.appSharedPrefs()
            .getInt(PREF_KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_NO)
    }
}
