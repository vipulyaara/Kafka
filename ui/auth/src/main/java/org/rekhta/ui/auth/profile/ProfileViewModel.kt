package org.rekhta.ui.auth.profile

import android.app.Application
import android.content.pm.PackageManager
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.User
import com.kafka.data.prefs.PreferencesStore
import com.kafka.data.prefs.THEME
import com.kafka.data.prefs.Theme
import com.kafka.data.prefs.observeTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.analytics.logger.Analytics
import org.kafka.auth.R
import org.kafka.base.domain.InvokeSuccess
import org.kafka.base.errorLog
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.collectStatus
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.common.snackbar.UiMessage
import org.kafka.domain.interactors.account.LogoutUser
import org.kafka.domain.observers.ObserveUser
import org.kafka.navigation.Navigator
import org.kafka.navigation.Screen
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val application: Application,
    private val snackbarManager: SnackbarManager,
    private val preferencesStore: PreferencesStore,
    private val logoutUser: LogoutUser,
    private val analytics: Analytics,
    private val navigator: Navigator,
    observeUser: ObserveUser
) : ViewModel() {
    private val loadingCounter = ObservableLoadingCounter()

    val state: StateFlow<ProfileViewState> = combine(
        observeUser.flow,
        preferencesStore.observeTheme(),
        loadingCounter.observable,
    ) { user, theme, isLoading ->
        ProfileViewState(
            currentUser = user,
            theme = theme,
            appVersion = getVersionName(),
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
        navigator.navigate(Screen.Feedback.createRoute(navigator.currentRoot.value))
    }

    fun openLogin() {
        analytics.log { openLogin() }
        navigator.navigate(Screen.Login.createRoute(navigator.currentRoot.value))
    }

    fun openLibrary() {
        navigator.navigate(Screen.Library.createRoute(navigator.currentRoot.value))
    }

    private fun getVersionName(): String? {
        return try {
            application.packageManager.getPackageInfo(application.packageName, 0).versionName
        } catch (e: PackageManager.NameNotFoundException) {
            errorLog { "Unable to get version name" }
            null
        }
    }
}

@Immutable
data class ProfileViewState(
    val currentUser: User? = null,
    val theme: Theme = Theme.DEFAULT,
    val appVersion: String? = null,
    val isLoading: Boolean = true
) {
    val userName: String
        get() = currentUser?.displayName.takeIf { it?.isNotEmpty() == true } ?: "Profile"
}
