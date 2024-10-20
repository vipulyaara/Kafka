package com.kafka.shared.home

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.common.widgets.LocalSnackbarHostState
import com.kafka.data.prefs.Theme
import com.kafka.navigation.Navigator
import com.kafka.navigation.NavigatorHost
import com.kafka.shared.RequestNotificationPermission
import com.kafka.shared.home.bottombar.HomeNavigation
import com.kafka.ui.components.snackbar.SnackbarMessagesHost
import com.kafka.shared.home.overlays.Overlays
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.ui.audio.AudioActionHost
import com.sarahang.playback.ui.audio.PlaybackHost
import com.sarahang.playback.ui.color.ColorExtractor
import com.sarahang.playback.ui.color.LocalColorExtractor
import kotlinx.coroutines.flow.collectLatest
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import tm.alashow.datmusic.downloader.Downloader
import tm.alashow.datmusic.ui.downloader.DownloaderHost
import ui.common.theme.theme.LocalTheme

typealias MainScreen = @Composable (NavHostController, BottomSheetNavigator, Theme) -> Unit

@Composable
@Inject
fun MainScreen(
    @Assisted navController: NavHostController,
    @Assisted bottomSheetNavigator: BottomSheetNavigator,
    @Assisted theme: Theme,
    colorExtractor: ColorExtractor,
    playbackConnection: PlaybackConnection,
    navigator: Navigator,
    downloader: Downloader,
    snackbarManager: SnackbarManager,
    viewModelFactory: () -> MainViewModel,
    home: HomeNavigation,
) {
    val mainViewModel = viewModel { viewModelFactory() }

    LaunchedEffect(mainViewModel, navController) {
        navController.currentBackStackEntryFlow.collectLatest { entry ->
            mainViewModel.logScreenView(entry)
        }
    }

    RequestNotificationPermission()

    CompositionLocalProvider(LocalColorExtractor provides colorExtractor) {
        CompositionLocalProvider(LocalTheme provides theme) {
            CompositionHosts(playbackConnection, navigator, downloader, snackbarManager) {
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
                    Overlays(mainViewModel = mainViewModel, snackbarManager = snackbarManager)
                    home(navController, mainViewModel.playerTheme)
                }
            }
        }
    }
}

@Composable
private fun CompositionHosts(
    playbackConnection: PlaybackConnection,
    navigator: Navigator,
    downloader: Downloader,
    snackbarManager: SnackbarManager,
    content: @Composable () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        NavigatorHost(navigator) {
            DownloaderHost(downloader) {
                PlaybackHost(playbackConnection) {
                    AudioActionHost(showMessage = { snackbarManager.addMessage(UiMessage.Plain(it)) }) {
                        SnackbarMessagesHost(snackbarManager = snackbarManager)
                        content()
                    }
                }
            }
        }
    }
}
