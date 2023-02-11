package com.kafka.user

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kafka.user.bottombar.HomeNavigationBar
import com.kafka.user.bottombar.ResizableHomeNavigationRail
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.isActive
import com.sarahang.playback.core.models.LocalPlaybackConnection
import com.sarahang.playback.ui.components.isWideLayout
import com.sarahang.playback.ui.player.mini.MiniPlayer
import org.kafka.common.widgets.LocalSnackbarHostState
import org.kafka.navigation.RootScreen
import org.kafka.navigation.selectRootScreen
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.snackbar.DismissableSnackbarHost
import ui.common.theme.theme.Dimens

@Composable
internal fun Home(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
    snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current,
) {
    val selectedTab by navController.currentScreenAsState()
    val playbackState by playbackConnection.playbackState.collectAsStateWithLifecycle()
    val nowPlaying by playbackConnection.nowPlaying.collectAsStateWithLifecycle()
    val isPlayerActive = (playbackState to nowPlaying).isActive

    BoxWithConstraints(modifier.fillMaxWidth()) {
        val isWideLayout = isWideLayout()
        val maxWidth = maxWidth
        Row(Modifier.fillMaxSize()) {
            if (isWideLayout)
                ResizableHomeNavigationRail(
                    availableWidth = maxWidth,
                    selectedTab = selectedTab,
                    navController = navController,
                    onPlayingTitleClick = { },
                    onPlayingArtistClick = { },
                )
            Scaffold(
                modifier = Modifier.weight(12f),
                snackbarHost = { DismissableSnackbarHost(snackbarHostState) },
                bottomBar = {
                    if (!isWideLayout)
                        Column {
                            MiniPlayer(
                                modifier = Modifier
                                    .padding(Dimens.Spacing08)
                                    .zIndex(2f),
                                openPlaybackSheet = { navController.navigate(RootScreen.PlayerLibrary.route) }
                            )

                            HomeNavigationBar(
                                selectedTab = selectedTab,
                                onNavigationSelected = { selected -> navController.selectRootScreen(selected) },
                                isPlayerActive = isPlayerActive,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    else Spacer(Modifier.navigationBarsPadding())
                }
            ) { paddings ->
                ProvideScaffoldPadding(paddings) {
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}

