@file:Suppress("EXPERIMENTAL_API_USAGE_FUTURE_ERROR")

package com.kafka.user.home

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.sarahang.playback.ui.audio.AudioActionHost
import com.sarahang.playback.ui.audio.PlaybackHost
import kotlinx.coroutines.flow.collectLatest
import org.kafka.analytics.Analytics
import org.kafka.common.widgets.LocalSnackbarHostState
import org.kafka.navigation.NavigatorHost
import org.kafka.navigation.rememberBottomSheetNavigator
import org.kafka.ui.components.snackbar.SnackbarMessagesHost
import tm.alashow.datmusic.ui.downloader.DownloaderHost

@Composable
fun MainScreen(analytics: Analytics) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)

    LaunchedEffect(navController, analytics) {
        navController.currentBackStackEntryFlow.collectLatest { entry ->
            analytics.logScreenView(
                label = entry.destination.displayName,
                route = entry.destination.route,
                arguments = entry.arguments,
            )
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        SnackbarMessagesHost()
        CompositionHosts {
//            val bottomSheetNavigator = rememberBottomSheetNavigator()
//            navController.navigatorProvider += bottomSheetNavigator
            ModalBottomSheetLayout(
                bottomSheetNavigator = bottomSheetNavigator,
                sheetShape = MaterialTheme.shapes.large.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp),
                ),
                sheetBackgroundColor = MaterialTheme.colorScheme.surface,
                sheetContentColor = MaterialTheme.colorScheme.onSurface,
                scrimColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.33f),
            ) {
                Home(navController)
            }
        }
    }
}

@Composable
private fun CompositionHosts(content: @Composable () -> Unit) {
    NavigatorHost {
        DownloaderHost {
            PlaybackHost {
                AudioActionHost {
                    content()
                }
            }
        }
    }
}
