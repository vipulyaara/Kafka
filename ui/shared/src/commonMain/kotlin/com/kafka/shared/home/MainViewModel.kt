package com.kafka.shared.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import com.kafka.analytics.providers.Analytics
import com.kafka.base.ApplicationInfo
import com.kafka.base.domain.onException
import com.kafka.base.extensions.stateInDefault
import com.kafka.data.entities.User
import com.kafka.data.prefs.PreferencesStore
import com.kafka.data.prefs.appMessageShownKey
import com.kafka.domain.interactors.account.SignInAnonymously
import com.kafka.domain.observers.ObserveAppMessage
import com.kafka.domain.observers.ObserveAppUpdateConfig
import com.kafka.domain.observers.account.ObserveUser
import com.kafka.networking.localizedMessage
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.getPlayerTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class MainViewModel(
    private val analytics: Analytics,
    private val signInAnonymously: SignInAnonymously,
    private val remoteConfig: RemoteConfig,
    private val preferencesStore: PreferencesStore,
    observeUser: ObserveUser,
    observeAppUpdateConfig: ObserveAppUpdateConfig,
    observeAppMessage: ObserveAppMessage,
    applicationInfo: ApplicationInfo
) : ViewModel() {
    val playerTheme by lazy { remoteConfig.getPlayerTheme() }
    var signInState by mutableStateOf(SignInState())

    private val versionCode = applicationInfo.versionCode
    val appUpdateConfig = observeAppUpdateConfig.flow
        .map {
            when {
                it.blockedAppVersions.contains(versionCode) -> AppUpdateState.Required
                it.forceUpdateVersion > 0 && versionCode < it.forceUpdateVersion -> AppUpdateState.Required
                it.softUpdateVersion > 0 && versionCode < it.softUpdateVersion -> AppUpdateState.Optional
                else -> AppUpdateState.None
            }
        }.stateInDefault(viewModelScope, AppUpdateState.None)

    val appMessage = observeAppMessage.flow
        .stateInDefault(viewModelScope, null)

    init {
        signInAnonymously()
        observeAppUpdateConfig(Unit)
        observeAppMessage(Unit)
        observeUser(ObserveUser.Params(true))

        viewModelScope.launch {
            combine(observeUser.flow, signInAnonymously.inProgress) { user, loading ->
                signInState = signInState.copy(user = user, loading = loading)
            }.collectLatest { }
        }
    }

    fun signInAnonymously() {
        signInState = signInState.copy(error = null)
        viewModelScope.launch {
            signInAnonymously(Unit)
                .onException { signInState = signInState.copy(error = it.message) }
        }
    }

    fun updateApp(context: Any?) {
        updateApp(context)
    }

    fun onAppMessageShown(id: String) {
        viewModelScope.launch {
            preferencesStore.save(appMessageShownKey(id), true)
            analytics.log { appMessageDismissed(id) }
        }
    }

    fun logScreenView(entry: NavBackStackEntry) {
        analytics.logScreenView(
            label = entry.destination.displayName,
            route = entry.destination.route,
            arguments = entry.arguments,
        )
    }

    enum class AppUpdateState {
        Required,
        Optional,
        None
    }

    data class SignInState(
        val user: User? = null,
        val loading: Boolean = false,
        val error: String? = null
    )
}
