package ui.common.theme

import androidx.lifecycle.ViewModel
import com.kafka.data.PreferenceKeys
import com.kafka.data.PreferencesStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    preferences: PreferencesStore
) : ViewModel() {

    val themeState = preferences.get(PreferenceKeys.THEME_STATE_KEY, true)
}
