package com.kafka.data.prefs

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.kafka.base.debug

@OptIn(FlowPreview::class)
class PreferencesStore(private val dataStore: DataStore<Preferences>) {
    suspend fun <T> remove(key: Preferences.Key<T>) {
        dataStore.edit { settings ->
            settings.remove(key)
        }
    }

    suspend fun <T> save(key: Preferences.Key<T>, value: T) {
        dataStore.edit { settings ->
            settings[key] = value
        }
    }

    val data: Flow<Preferences>
        get() = dataStore.data

    fun <T> get(key: Preferences.Key<T>, defaultValue: T): Flow<T> = dataStore.data
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
