package com.kafka.user.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.getPlayerTheme
import com.kafka.remote.config.minSupportedVersion
import com.kafka.user.BuildConfig
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.kafka.analytics.logger.Analytics
import org.kafka.common.goToPlayStore
import com.kafka.domain.interactors.account.SignInAnonymously
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val analytics: Analytics,
    private val signInAnonymously: SignInAnonymously,
    private val remoteConfig: RemoteConfig,
) : ViewModel() {
    val playerTheme by lazy { remoteConfig.getPlayerTheme() }
    val isUpdateRequired by lazy {
        val minSupportedVersion = remoteConfig.minSupportedVersion()
        minSupportedVersion != 0L && BuildConfig.VERSION_CODE < minSupportedVersion
    }

    init {
        signInAnonymously()
    }

    private fun signInAnonymously() {
        viewModelScope.launch {
            signInAnonymously(Unit).collect()
        }
    }

    fun updateApp(context: Context) {
        context.goToPlayStore()
    }

    fun logScreenView(entry: NavBackStackEntry) {
        analytics.logScreenView(
            label = entry.destination.displayName,
            route = entry.destination.route,
            arguments = entry.arguments,
        )
    }
}
