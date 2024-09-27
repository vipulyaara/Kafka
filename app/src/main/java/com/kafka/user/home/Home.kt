package com.kafka.user.home

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kafka.user.home.bottombar.HomeNavigationBar
import com.kafka.user.home.bottombar.rail.ResizableHomeNavigationRail
import com.kafka.user.home.bottombar.rail.ResizableHomeNavigationRailViewModel
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.artist
import com.sarahang.playback.core.isActive
import com.sarahang.playback.core.models.LocalPlaybackConnection
import com.sarahang.playback.ui.components.isWideLayout
import com.sarahang.playback.ui.player.mini.MiniPlayer
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.kafka.analytics.logger.Analytics
import org.kafka.common.widgets.LocalSnackbarHostState
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.currentScreenAsState
import org.kafka.navigation.graph.RootScreen
import org.kafka.navigation.graph.Screen
import org.kafka.navigation.graph.navigationRoute
import org.kafka.navigation.selectRootScreen
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.snackbar.DismissableSnackbarHost
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.shouldUseDarkColors

typealias Home = @Composable (NavHostController, String) -> Unit

@Composable
@Inject
internal fun Home(
    @Assisted navController: NavHostController,
    @Assisted playerTheme: String,
    analytics: Analytics,
    resizableViewModelFactory: () -> ResizableHomeNavigationRailViewModel,
    modifier: Modifier = Modifier,
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
    snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current,
    appNavigation: AppNavigation,
) {
    val selectedTab by navController.currentScreenAsState()
    val playbackState by playbackConnection.playbackState.collectAsStateWithLifecycle()
    val nowPlaying by playbackConnection.nowPlaying.collectAsStateWithLifecycle()
    val isPlayerActive = (playbackState to nowPlaying).isActive

    BoxWithConstraints(modifier.fillMaxWidth()) {
        val isWideLayout = isWideLayout()
        val maxWidth = maxWidth

        Row(Modifier.fillMaxSize()) {
            if (isWideLayout && shouldShowBottomBar(navController)) {
                val resizableViewModel = viewModel { resizableViewModelFactory() }
                ResizableHomeNavigationRail(
                    availableWidth = maxWidth,
                    selectedTab = selectedTab,
                    navController = navController,
                    analytics = analytics,
                    viewModel = resizableViewModel,
                    onPlayingArtistClick = {
                        navController.navigate(Screen.Search(nowPlaying.artist.orEmpty()))
                    },
                )
            }
            Scaffold(
                modifier = Modifier.weight(12f),
                snackbarHost = { DismissableSnackbarHost(snackbarHostState) },
                bottomBar = {
                    if (shouldShowBottomBar(navController)) {
                        BottomBar(
                            isWideLayout = isWideLayout,
                            navController = navController,
                            selectedTab = selectedTab,
                            isPlayerActive = isPlayerActive,
                            analytics = analytics,
                            playerTheme = playerTheme
                        )
                    }
                }
            ) { paddings ->
                ProvideScaffoldPadding(paddings) {
                    appNavigation(navController)
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
    isPlayerActive: Boolean,
    analytics: Analytics,
    playerTheme: String,
) {
    val navigator = LocalNavigator.current
    if (!isWideLayout)
        Column {
            MiniPlayer(
                useDarkTheme = LocalTheme.current.shouldUseDarkColors(),
                modifier = Modifier
                    .padding(Dimens.Spacing08)
                    .zIndex(2f),
                openPlaybackSheet = { navigator.navigate(Screen.Player) },
                playerTheme = playerTheme,
            )

            HomeNavigationBar(
                selectedTab = selectedTab,
                onNavigationSelected = { selected ->
                    analytics.log { homeTabSwitched(selectedTab.analyticsKey, "navigation_bar") }
                    navController.selectRootScreen(selected)
                },
                isPlayerActive = isPlayerActive,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    else Spacer(Modifier.navigationBarsPadding())
}

@Composable
private fun shouldShowBottomBar(navController: NavController): Boolean {
    val currentRoute by navController.currentBackStackEntryAsState()
    val destination by remember(currentRoute) {
        derivedStateOf { currentRoute?.destination?.route?.substringBefore("/") }
    }

    return destination != Screen.Reader.navigationRoute
            && destination != Screen.OnlineReader.navigationRoute
}
