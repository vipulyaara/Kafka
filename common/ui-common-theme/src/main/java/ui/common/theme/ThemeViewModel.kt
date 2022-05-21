package ui.common.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.kafka.data.PreferenceKeys
import com.kafka.data.PreferencesStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.rekhta.analytics.event
import ui.common.theme.theme.DefaultTheme
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val preferences: PreferencesStore,
    private val analytics: FirebaseAnalytics
) : ViewModel() {

    val themeState =
        preferences.get(PreferenceKeys.THEME_STATE_KEY, ThemeState.serializer(), DefaultTheme)

    fun applyThemeState(themeState: ThemeState) {
        analytics.event(
            "theme.apply",
            mapOf(
                "darkMode" to themeState.isDarkMode,
                "palette" to themeState.colorPalettePreference.name
            )
        )
        viewModelScope.launch {
            preferences.save(PreferenceKeys.THEME_STATE_KEY, themeState, ThemeState.serializer())
        }
    }
}
