package com.kafka.profile

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.logger.Analytics
import com.kafka.base.domain.InvokeSuccess
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.ObservableLoadingCounter
import com.kafka.common.collectStatus
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage.Plain
import com.kafka.data.entities.User
import com.kafka.data.platform.app.AppVersionInfo
import com.kafka.data.prefs.PreferencesStore
import com.kafka.data.prefs.SAFE_MODE
import com.kafka.data.prefs.SAFE_MODE_DEFAULT
import com.kafka.data.prefs.THEME
import com.kafka.data.prefs.TRUE_CONTRAST
import com.kafka.data.prefs.TRUE_CONTRAST_DEFAULT
import com.kafka.data.prefs.Theme
import com.kafka.data.prefs.observeSafeMode
import com.kafka.data.prefs.observeTheme
import com.kafka.data.prefs.observeTrueContrast
import com.kafka.domain.interactors.account.LogoutUser
import com.kafka.domain.observers.ObserveUser
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.Screen
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val snackbarManager: SnackbarManager,
    private val preferencesStore: PreferencesStore,
    private val logoutUser: LogoutUser,
    private val analytics: Analytics,
    private val navigator: Navigator,
    private val appVersionInfo: AppVersionInfo,
    observeUser: ObserveUser,
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()

    val state: StateFlow<ProfileViewState> = combine(
        observeUser.flow,
        preferencesStore.observeTheme(),
        preferencesStore.observeTrueContrast(),
        preferencesStore.observeSafeMode(),
        loadingCounter.observable,
    ) { user, theme, trueContrast, safeMode, isLoading ->
        ProfileViewState(
            currentUser = user,
            theme = theme,
            trueContrast = trueContrast,
            safeMode = safeMode,
            appVersion = appVersionInfo.versionName,
            isLoading = isLoading
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = ProfileViewState(),
    )

    init {
        observeUser(ObserveUser.Params())
    }

    fun toggleTheme() {
        viewModelScope.launch {
            val newTheme = when (state.value.theme) {
                Theme.SYSTEM -> Theme.DARK
                Theme.DARK -> Theme.LIGHT
                Theme.LIGHT -> Theme.SYSTEM
            }.name

            preferencesStore.save(THEME, newTheme)
            analytics.log { themeChanged(newTheme) }
        }
    }

    fun toggleTrueContrast() {
        viewModelScope.launch {
            preferencesStore.save(TRUE_CONTRAST, !state.value.trueContrast)
            analytics.log { trueContrastChanged(!state.value.trueContrast) }
        }
    }

    fun toggleSafeMode() {
        viewModelScope.launch {
            preferencesStore.save(SAFE_MODE, !state.value.safeMode)
            analytics.log { safeModeChanged(!state.value.safeMode) }
        }
    }

    fun logOpenNotificationSettings() {
        analytics.log { openNotificationsSettings() }
    }

    fun logout(onLogout: () -> Unit = { navigator.goBack() }) {
        analytics.log { logoutClicked() }
        viewModelScope.launch {
            logoutUser(Unit).collectStatus(loadingCounter, snackbarManager) { status ->
                if (status == InvokeSuccess) {
                    snackbarManager.addMessage(Plain("Logged out successfully"))
                    onLogout()
                }
            }
        }
    }

    fun openFeedback() {
        navigator.goBack()
        navigator.navigate(Screen.Feedback)
    }

    fun openLogin() {
        analytics.log { openLogin() }
        navigator.navigate(Screen.Login)
    }

    fun openLibrary() {
        navigator.navigate(Screen.Library)
    }

}

@Immutable
data class ProfileViewState(
    val currentUser: User? = null,
    val theme: Theme = Theme.DEFAULT,
    val trueContrast: Boolean = TRUE_CONTRAST_DEFAULT,
    val safeMode: Boolean = SAFE_MODE_DEFAULT,
    val appVersion: String? = null,
    val isLoading: Boolean = true,
)

const val trueContrastPrefEnabled = false