package com.kafka.data

import androidx.datastore.preferences.core.stringPreferencesKey
import com.kafka.data.injection.ProcessLifetime
import com.kafka.data.prefs.PreferencesStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import org.kafka.base.extensions.stateInDefault
import javax.inject.Inject

class MediaTypeManager @Inject constructor(
    private val preferencesStore: PreferencesStore,
    @ProcessLifetime private val coroutineScope: CoroutineScope
) {
    private val preferenceKey get() = stringPreferencesKey("media_type")

    private val mediaTypeState = preferencesStore.getStateFlow(
        keyName = preferenceKey, scope = coroutineScope, initialValue = MediaType.Audio.name
    )

    private val mediaTypeFlow = mediaTypeState.map { MediaType.valueOf(it) }
        .stateInDefault(coroutineScope, MediaType.Audio)

    val mediaType: MediaType = mediaTypeFlow.value

    fun updateMediaType(mediaType: MediaType) {
        mediaTypeState.value = mediaType.name
    }
}

enum class MediaType {
    Audio, Texts
}
