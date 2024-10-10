package com.kafka.user.home.overlays

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.asString
import com.kafka.data.model.AppMessage
import com.kafka.navigation.LocalNavigator
import com.kafka.navigation.graph.Screen
import com.kafka.user.R
import com.kafka.user.home.MainViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
internal fun Overlays(mainViewModel: MainViewModel, snackbarManager: SnackbarManager) {
    val context = LocalContext.current

    val appUpdateConfig by mainViewModel.appUpdateConfig.collectAsStateWithLifecycle()
    AppUpdate(
        appUpdateConfig = appUpdateConfig,
        snackbarManager = snackbarManager,
        updateApp = { mainViewModel.updateApp(context) })

    val appMessage by mainViewModel.appMessage.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current

    fun onPrimaryClick(appMessage: AppMessage) {
        if (appMessage.primaryUrl.isNotEmpty()) {
            navigator.navigate(Screen.Web(appMessage.primaryUrl))
        }
        mainViewModel.onAppMessageShown(appMessage.id)
    }

    AppMessage(
        appMessage = appMessage,
        snackbarManager = snackbarManager,
        onPrimaryClick = { onPrimaryClick(appMessage!!) },
        dismiss = { mainViewModel.onAppMessageShown(appMessage!!.id) }
    )

    LaunchedEffect(snackbarManager.actionPerformed) {
        snackbarManager.actionPerformed.collectLatest { message ->
            when (message.message.asString()) {
                context.getString(R.string.app_update_is_available) -> {
                    mainViewModel.updateApp(context)
                }

                appMessage?.snackbarMessage -> {
                    onPrimaryClick(appMessage!!)
                }
            }
        }
    }
}