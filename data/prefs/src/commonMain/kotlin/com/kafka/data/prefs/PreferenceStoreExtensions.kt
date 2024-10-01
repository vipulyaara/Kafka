package com.kafka.data.prefs

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val THEME = stringPreferencesKey("theme")
val TRUE_CONTRAST = booleanPreferencesKey("true_contrast")
val SAFE_MODE = booleanPreferencesKey("safe_mode")

fun PreferencesStore.observeTheme(): Flow<Theme> {
    return get(THEME, Theme.DEFAULT.name)
        .map { theme ->
            Theme.entries.firstOrNull { it.name.equals(theme, true) } ?: Theme.SYSTEM
        }
}

fun PreferencesStore.observeTrueContrast(): Flow<Boolean> {
    return get(TRUE_CONTRAST, TRUE_CONTRAST_DEFAULT)
}

fun PreferencesStore.observeSafeMode(): Flow<Boolean> {
    return get(SAFE_MODE, SAFE_MODE_DEFAULT)
}

enum class Theme {
    LIGHT,
    DARK,
    SYSTEM;

    companion object {
        val DEFAULT = SYSTEM
    }
}

const val TRUE_CONTRAST_DEFAULT = true
const val SAFE_MODE_DEFAULT = false
