package com.kafka.reader.epub.settings

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object ReaderPreferences {
    val FONT_SCALE = floatPreferencesKey("reader_font_scale")
    val LINE_HEIGHT = stringPreferencesKey("reader_line_height")
    val FONT_STYLE = stringPreferencesKey("reader_font_style")
    val MARGIN_SCALE = floatPreferencesKey("reader_margin_scale")
    val BACKGROUND_COLOR = stringPreferencesKey("reader_background_color")
    val IS_DARK_MODE = booleanPreferencesKey("reader_is_dark_mode")
    val READING_MODE = booleanPreferencesKey("reader_reading_mode")
    val TEXT_ALIGNMENT = stringPreferencesKey("reader_text_alignment")
}
