package com.kafka.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.kafka.base.debug
import javax.inject.Inject

private const val STORE_NAME = "app_preferences"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)

class PreferencesStore @Inject constructor(@ApplicationContext private val context: Context) {

    suspend fun <T> remove(key: Preferences.Key<T>) {
        context.dataStore.edit { settings ->
            settings.remove(key)
        }
    }

    suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { settings ->
            settings[key] = value
        }
    }

    val data: Flow<Preferences>
        get() = context.dataStore.data

    fun <T> get(key: Preferences.Key<T>, defaultValue: T): Flow<T> = context.dataStore.data
        .map { preferences -> preferences[key] ?: return@map defaultValue }

    fun <T> getStateFlow(
        keyName: Preferences.Key<T>,
        scope: CoroutineScope,
        initialValue: T,
        saveDebounce: Long = 200,
    ): MutableStateFlow<T> {
        val state = MutableStateFlow(initialValue)
        scope.launch {
            state.value = get(keyName, initialValue).first()
            state.debounce(saveDebounce)
                .collectLatest {
                    debug { "Saving firebase topic store: $keyName $it" }
                    save(keyName, it)
                }
        }
        return state
    }
}

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
    return get(TRUE_CONTRAST, true)
}

fun PreferencesStore.observeSafeMode(): Flow<Boolean> {
    return get(SAFE_MODE, false)
}

enum class Theme {
    LIGHT,
    DARK,
    SYSTEM;

    companion object {
        val DEFAULT = SYSTEM
    }
}

enum class ContentType {
    AUDIO,
    TEXT,
    BOTH;

    val mediaTypes
        get() = when (this) {
            AUDIO -> listOf("audio")
            TEXT -> listOf("texts")
            BOTH -> listOf("texts", "audio")
        }

    companion object {
        val DEFAULT = BOTH
    }
}
