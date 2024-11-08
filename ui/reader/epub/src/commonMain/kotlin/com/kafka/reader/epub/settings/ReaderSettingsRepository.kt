package com.kafka.reader.epub.settings

import androidx.datastore.preferences.core.edit
import com.kafka.data.prefs.PreferencesStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class ReaderSettingsRepository(private val preferencesStore: PreferencesStore) {
    fun getSettings(): Flow<ReaderSettings> = preferencesStore.data.map { prefs ->
        val isDarkMode = prefs[ReaderPreferences.IS_DARK_MODE] ?: false //todo: use system theme
        ReaderSettings(
            fontScale = prefs[ReaderPreferences.FONT_SCALE] ?: 1f,
            lineHeightType = prefs[ReaderPreferences.LINE_HEIGHT]?.let {
                ReaderSettings.LineHeight.from(it)
            } ?: ReaderSettings.LineHeight.NORMAL,
            marginScale = prefs[ReaderPreferences.MARGIN_SCALE] ?: 1f,
            isDarkMode = isDarkMode,
            isReadingMode = prefs[ReaderPreferences.READING_MODE] ?: false,
            fontStyle = prefs[ReaderPreferences.FONT_STYLE]?.let {
                ReaderSettings.FontStyle.fromString(it)
            } ?: ReaderSettings.FontStyle.Default,
            background = prefs[ReaderPreferences.BACKGROUND_COLOR]?.let {
                ReaderSettings.Background.fromString(it)
            } ?: ReaderSettings.Background.default(isDarkMode),
            textAlignment = prefs[ReaderPreferences.TEXT_ALIGNMENT]?.let {
                ReaderSettings.TextAlignment.from(it)
            } ?: ReaderSettings.TextAlignment.LEFT
        )
    }

    suspend fun updateSettings(settings: ReaderSettings) {
        preferencesStore.dataStore.edit { prefs ->
            prefs[ReaderPreferences.FONT_SCALE] = settings.fontScale
            prefs[ReaderPreferences.LINE_HEIGHT] = settings.lineHeightType.name
            prefs[ReaderPreferences.MARGIN_SCALE] = settings.marginScale
            prefs[ReaderPreferences.IS_DARK_MODE] = settings.isDarkMode
            prefs[ReaderPreferences.READING_MODE] = settings.isReadingMode
            prefs[ReaderPreferences.FONT_STYLE] = settings.fontStyle.toString()
            prefs[ReaderPreferences.BACKGROUND_COLOR] = settings.background.toString()
            prefs[ReaderPreferences.TEXT_ALIGNMENT] = settings.textAlignment.name
        }
    }
}
