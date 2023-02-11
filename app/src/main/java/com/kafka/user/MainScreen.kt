@file:Suppress("EXPERIMENTAL_API_USAGE_FUTURE_ERROR")

package com.kafka.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.sarahang.playback.ui.audio.AudioActionHost
import com.sarahang.playback.ui.audio.PlaybackHost
import kotlinx.coroutines.flow.collectLatest
import org.kafka.analytics.Logger
import org.kafka.common.widgets.LocalSnackbarHostState
import org.kafka.navigation.NavigatorHost
import org.kafka.navigation.rememberBottomSheetNavigator
import org.kafka.ui.components.snackbar.SnackbarMessagesHost
import tm.alashow.datmusic.ui.downloader.DownloaderHost

@Composable
fun MainScreen(
    analytics: Logger,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val navController = rememberAnimatedNavController()

    LaunchedEffect(navController, analytics) {
        navController.currentBackStackEntryFlow.collectLatest { entry ->
            analytics.logScreenView(
                label = entry.destination.displayName,
                route = entry.destination.route,
                arguments = entry.arguments,
            )
        }
    }

    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        SnackbarMessagesHost()
        NavigatorHost {
            DownloaderHost {
                PlaybackHost {
                    AudioActionHost {
                        val bottomSheetNavigator = rememberBottomSheetNavigator()
                        navController.navigatorProvider += bottomSheetNavigator
                        ModalBottomSheetLayout(bottomSheetNavigator, Modifier.fillMaxSize()) {
                            Home(navController)
                        }
                    }
                }
            }
        }
    }
}
