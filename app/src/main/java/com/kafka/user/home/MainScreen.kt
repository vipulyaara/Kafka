package com.kafka.user.home

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.sarahang.playback.ui.audio.AudioActionHost
import com.sarahang.playback.ui.audio.PlaybackHost
import org.kafka.common.widgets.LocalSnackbarHostState
import org.kafka.navigation.NavigatorHost
import org.kafka.navigation.rememberBottomSheetNavigator
import org.kafka.ui.components.snackbar.SnackbarMessagesHost
import tm.alashow.datmusic.ui.downloader.DownloaderHost

@Composable
fun MainScreen() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    val mainViewModel = hiltViewModel<MainViewModel>()

    LaunchedEffect(mainViewModel, navController) {
        mainViewModel.initializeScreenOpen(navController)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        SnackbarMessagesHost()
        CompositionHosts {
            ModalBottomSheetLayout(
                bottomSheetNavigator = bottomSheetNavigator,
                sheetShape = MaterialTheme.shapes.large.copy(
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp),
                ),
                sheetBackgroundColor = MaterialTheme.colorScheme.surface,
                sheetContentColor = MaterialTheme.colorScheme.onSurface,
                scrimColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.32f),
            ) {
                Home(navController = navController, analytics = mainViewModel.analytics)
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
