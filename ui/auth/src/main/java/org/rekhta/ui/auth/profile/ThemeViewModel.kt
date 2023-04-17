package org.rekhta.ui.auth.profile

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.prefs.PreferencesStore
import dagger.hilt.android.lifecycle.HiltViewModel
import org.kafka.analytics.Analytics
import ui.common.theme.theme.Theme
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val analytics: Analytics,
    preferencesStore: PreferencesStore
) : ViewModel() {
    private val themePrefsKey = stringPreferencesKey("theme")

    val currentTheme = preferencesStore.getStateFlow(themePrefsKey, viewModelScope, Theme.Dark.name)

    fun updateTheme(theme: Theme) {
        analytics.log { this.themeChanged(theme.name) }
        currentTheme.value = theme.name
    }
}
