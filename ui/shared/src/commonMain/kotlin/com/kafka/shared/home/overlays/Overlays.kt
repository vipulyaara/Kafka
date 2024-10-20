package com.kafka.shared.home.overlays

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.getContext
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.asString
import com.kafka.data.model.AppMessage
import com.kafka.navigation.LocalNavigator
import com.kafka.navigation.graph.Screen
import com.kafka.shared.home.MainViewModel
import com.kafka.user.home.overlays.AppMessage
import com.kafka.user.home.overlays.AppUpdate
import kafka.ui.shared.generated.resources.Res
import kafka.ui.shared.generated.resources.app_update_is_available
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Overlays(mainViewModel: MainViewModel, snackbarManager: SnackbarManager) {
    val appUpdateConfig by mainViewModel.appUpdateConfig.collectAsStateWithLifecycle()
    val context = getContext()

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

    val appUpdateAvailableString = stringResource(Res.string.app_update_is_available)
    LaunchedEffect(snackbarManager.actionPerformed) {
        snackbarManager.actionPerformed.collectLatest { message ->
            when (message.message.asString()) {
                appUpdateAvailableString -> {
                    mainViewModel.updateApp(context)
                }

                appMessage?.snackbarMessage -> {
                    onPrimaryClick(appMessage!!)
                }
            }
        }
    }
}