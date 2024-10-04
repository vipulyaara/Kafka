package com.rekhta.ui.profile

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.User
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.kafka.analytics.logger.Analytics
import com.kafka.base.combine
import com.kafka.base.domain.InvokeSuccess
import com.kafka.base.errorLog
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.ObservableLoadingCounter
import com.kafka.common.collectStatus
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.domain.interactors.account.LogoutUser
import com.kafka.domain.observers.ObserveUser
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.Screen
import com.kafka.profile.R
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val application: Application,
    private val snackbarManager: SnackbarManager,
    private val preferencesStore: PreferencesStore,
    private val logoutUser: LogoutUser,
    private val analytics: Analytics,
    private val navigator: Navigator,
    observeUser: ObserveUser,
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()
    private val notificationsEnabled = MutableStateFlow(true)

    val state: StateFlow<ProfileViewState> = combine(
        observeUser.flow,
        preferencesStore.observeTheme(),
        preferencesStore.observeTrueContrast(),
        preferencesStore.observeSafeMode(),
        notificationsEnabled,
        loadingCounter.observable,
    ) { user, theme, trueContrast, safeMode, notificationsEnabled, isLoading ->
        ProfileViewState(
            currentUser = user,
            theme = theme,
            trueContrast = trueContrast,
            safeMode = safeMode,
            appVersion = getVersionName(),
            notificationsEnabled = notificationsEnabled,
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

    fun logout(onLogout: () -> Unit = { navigator.goBack() }) {
        analytics.log { logoutClicked() }
        viewModelScope.launch {
            logoutUser(Unit).collectStatus(loadingCounter, snackbarManager) { status ->
                if (status == InvokeSuccess) {
                    snackbarManager.addMessage(UiMessage(R.string.logged_out))
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

    private fun getVersionName(): String? {
        return try {
            application.packageManager.getPackageInfo(application.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            errorLog { "Unable to get version name" }
            null
        }
    }

    internal fun determineNotificationsEnabled() {
        val notificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationsEnabled.value = notificationManager.areNotificationsEnabled()
    }

    internal fun openNotificationSettings(context: Context) {
        analytics.log { openNotificationsSettings() }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            context.startActivity(intent)
        }
    }
}

@Immutable
data class ProfileViewState(
    val currentUser: User? = null,
    val theme: Theme = Theme.DEFAULT,
    val trueContrast: Boolean = TRUE_CONTRAST_DEFAULT,
    val safeMode: Boolean = SAFE_MODE_DEFAULT,
    val notificationsEnabled: Boolean = true,
    val appVersion: String? = null,
    val isLoading: Boolean = true,
)

const val trueContrastPrefEnabled = false