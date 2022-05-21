package com.kafka.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import javax.inject.Inject

private const val STORE_NAME = "app_preferences"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)

val DEFAULT_JSON_FORMAT = Json {
    ignoreUnknownKeys = true
}

private val format = DEFAULT_JSON_FORMAT

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

    fun <T> get(key: Preferences.Key<T>, defaultValue: T): Flow<T> = context.dataStore.data
        .map { preferences -> preferences[key] ?: return@map defaultValue }

    fun get(key: Preferences.Key<String>, defaultValue: String): Flow<String> =
        context.dataStore.data
            .map { preferences -> preferences[key] ?: return@map defaultValue }

    suspend fun <T> save(name: String, value: T, serializer: KSerializer<T>) {
        val key = stringPreferencesKey(name)
        save(key, Json.encodeToString(serializer, value))
    }

    fun <T> get(name: String, serializer: KSerializer<T>, defaultValue: T): Flow<T> {
        return get(stringPreferencesKey(name), "").map {
            try {
                format.decodeFromString(serializer, it)
            } catch (ex: Exception) {
                defaultValue
            }
        }
    }
}

object PreferenceKeys {
    const val THEME_STATE_KEY = "theme_state"
}
