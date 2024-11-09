package com.kafka.reader.epub.domain

import androidx.datastore.preferences.core.edit
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.prefs.PreferencesStore
import com.kafka.reader.epub.settings.ReaderSettings
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateReaderSettings(
    private val preferencesStore: PreferencesStore,
    private val json: Json,
    private val dispatchers: CoroutineDispatchers
) : Interactor<ReaderSettings, Unit>() {

    override suspend fun doWork(params: ReaderSettings) {
        withContext(dispatchers.io) {
            preferencesStore.dataStore.edit { prefs ->
                prefs[SETTINGS] = json.encodeToString(
                    serializer = ReaderSettings.serializer(),
                    value = params
                )
            }
        }
    }
}
