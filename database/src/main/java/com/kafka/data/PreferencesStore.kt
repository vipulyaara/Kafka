package com.kafka.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import org.rekhta.base.network.model.Language
import org.rekhta.base.network.model.Optional
import org.rekhta.base.network.model.some
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

    fun <T> optional(key: Preferences.Key<T>): Flow<Optional<T>> = context.dataStore.data
        .map { preferences -> some(preferences[key]) }

    suspend fun <T> save(name: String, value: T, serializer: KSerializer<T>) {
        val key = stringPreferencesKey(name)
        save(key, Json.encodeToString(serializer, value))
    }

    fun <T> optional(name: String, serializer: KSerializer<T>): Flow<Optional<T>> {
        val key = stringPreferencesKey(name)
        return optional(key).map {
            when (it) {
                is Optional.Some<String> ->
                    try {
                        some(format.decodeFromString(serializer, it.value))
                    } catch (e: SerializationException) {
                        Optional.None
                    }
                else -> Optional.None
            }
        }
    }

    fun <T> get(name: String, serializer: KSerializer<T>, defaultValue: T): Flow<T> {
        return optional(name, serializer).map {
            when (it) {
                is Optional.None -> defaultValue
                else -> it.value()
            }
        }
    }
}

object PreferenceKeys {
    const val THEME_STATE_KEY = "theme_state"
    const val LANGUAGE_KEY = "language"
}

val PreferencesStore.language
    get() = get(PreferenceKeys.LANGUAGE_KEY, Language.serializer(), Language.English)
        .distinctUntilChanged()

suspend fun PreferencesStore.saveLanguage(language: Language) =
    save(PreferenceKeys.LANGUAGE_KEY, language, Language.serializer())
