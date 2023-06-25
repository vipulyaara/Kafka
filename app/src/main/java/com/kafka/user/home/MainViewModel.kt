package com.kafka.user.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.kafka.analytics.logger.Analytics
import org.kafka.domain.interactors.account.SignInAnonymously
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val analytics: Analytics,
    private val signInAnonymously: SignInAnonymously
) : ViewModel() {

    init {
        signInAnonymously()
    }

    private fun signInAnonymously() {
        viewModelScope.launch {
            signInAnonymously(Unit).collect()
        }
    }

    fun logScreenView(entry: NavBackStackEntry) {
        analytics.logScreenView(
            label = entry.destination.displayName,
            route = entry.destination.route,
            arguments = entry.arguments,
        )
    }
}
