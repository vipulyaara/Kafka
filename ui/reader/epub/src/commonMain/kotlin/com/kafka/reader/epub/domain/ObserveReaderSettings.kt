package com.kafka.reader.epub.domain

import androidx.datastore.preferences.core.stringPreferencesKey
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.prefs.PreferencesStore
import com.kafka.reader.epub.settings.ReaderSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveReaderSettings(
    private val preferencesStore: PreferencesStore,
    private val itemDetailDao: ItemDetailDao,
    private val json: Json
) : SubjectInteractor<ObserveReaderSettings.Params, ReaderSettings>() {

    override fun createObservable(params: Params): Flow<ReaderSettings> {
        return combine(
            itemDetailDao.observeItemDetail(params.itemId).filterNotNull(),
            preferencesStore.data
        ) { item, prefs ->
            prefs[SETTINGS]?.let { jsonString ->
                try {
                    json.decodeFromString<ReaderSettings>(jsonString)
                } catch (e: Exception) {
                    ReaderSettings.default(item.language)
                }
            } ?: ReaderSettings.default(item.language)
        }
    }

    data class Params(val itemId: String)
}

val SETTINGS = stringPreferencesKey("settings")
