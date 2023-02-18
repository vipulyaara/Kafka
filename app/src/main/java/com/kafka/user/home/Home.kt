package com.kafka.user.home

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.kafka.user.bottombar.HomeNavigationBar
import com.kafka.user.bottombar.rail.ResizableHomeNavigationRail
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.artist
import com.sarahang.playback.core.isActive
import com.sarahang.playback.core.models.LocalPlaybackConnection
import com.sarahang.playback.ui.components.isWideLayout
import com.sarahang.playback.ui.player.mini.MiniPlayer
import org.kafka.common.widgets.LocalSnackbarHostState
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.RootScreen
import org.kafka.navigation.Screen
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
    val navigator = LocalNavigator.current
    val playbackState by playbackConnection.playbackState.collectAsStateWithLifecycle()
    val nowPlaying by playbackConnection.nowPlaying.collectAsStateWithLifecycle()
    val isPlayerActive = (playbackState to nowPlaying).isActive

    LaunchedEffect(selectedTab) {
        navigator.updateRoot(selectedTab)
    }

    BoxWithConstraints(modifier.fillMaxWidth()) {
        val isWideLayout = isWideLayout()
        val maxWidth = maxWidth

        Row(Modifier.fillMaxSize()) {
            HomeNavigationRail(
                isWideLayout = isWideLayout,
                maxWidth = maxWidth,
                selectedTab = selectedTab,
                navController = navController,
                artist = nowPlaying.artist
            )
            Scaffold(
                modifier = Modifier.weight(12f),
                snackbarHost = { DismissableSnackbarHost(snackbarHostState) },
                bottomBar = {
                    BottomBar(
                        isWideLayout = isWideLayout,
                        navController = navController,
                        selectedTab = selectedTab,
                        isPlayerActive = isPlayerActive
                    )
                }
            ) { paddings ->
                ProvideScaffoldPadding(paddings) {
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}

@Composable
private fun BottomBar(
    isWideLayout: Boolean,
    navController: NavHostController,
    selectedTab: RootScreen,
    isPlayerActive: Boolean
) {
    val navigator = LocalNavigator.current
    val currentRoot by navigator.currentRoot.collectAsStateWithLifecycle()

    if (!isWideLayout)
        Column {
            MiniPlayer(
                modifier = Modifier
                    .padding(Dimens.Spacing08)
                    .zIndex(2f),
                openPlaybackSheet = {
                    navigator.navigate(Screen.Player.createRoute(currentRoot))
                }
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

@Composable
private fun RowScope.HomeNavigationRail(
    isWideLayout: Boolean,
    maxWidth: Dp,
    selectedTab: RootScreen,
    navController: NavHostController,
    artist: String?
) {
    if (isWideLayout) {
        ResizableHomeNavigationRail(
            availableWidth = maxWidth,
            selectedTab = selectedTab,
            navController = navController,
            onPlayingArtistClick = {
                navController.navigate(Screen.Search.createRoute(RootScreen.Search, artist))
            },
        )
    }
}
