package com.kafka.shared.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import com.kafka.analytics.logger.Analytics
import com.kafka.base.ApplicationInfo
import com.kafka.base.extensions.stateInDefault
import com.kafka.data.prefs.PreferencesStore
import com.kafka.data.prefs.appMessageShownKey
import com.kafka.domain.interactors.account.SignInAnonymously
import com.kafka.domain.observers.ObserveAppMessage
import com.kafka.domain.observers.ObserveAppUpdateConfig
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.getPlayerTheme
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val analytics: Analytics,
    private val signInAnonymously: SignInAnonymously,
    private val remoteConfig: RemoteConfig,
    private val preferencesStore: PreferencesStore,
    observeAppUpdateConfig: ObserveAppUpdateConfig,
    observeAppMessage: ObserveAppMessage,
    applicationInfo: ApplicationInfo
) : ViewModel() {
    val playerTheme by lazy { remoteConfig.getPlayerTheme() }

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
    }

    private fun signInAnonymously() {
        viewModelScope.launch {
            signInAnonymously(Unit).collect()
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
}
