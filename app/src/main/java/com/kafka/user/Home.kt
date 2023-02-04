package com.kafka.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.kafka.user.bottombar.HomeNavigationBar
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.isActive
import com.sarahang.playback.core.models.LocalPlaybackConnection
import com.sarahang.playback.ui.player.mini.MiniPlayer
import org.kafka.navigation.RootScreen
import org.kafka.ui.components.ProvideScaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
internal fun Home(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
) {
    val selectedTab by navController.currentScreenAsState()
    val playbackState by playbackConnection.playbackState.collectAsStateWithLifecycle()
    val nowPlaying by playbackConnection.nowPlaying.collectAsStateWithLifecycle()
    val isPlayerActive = (playbackState to nowPlaying).isActive

    Scaffold(
        modifier = modifier.fillMaxWidth(),
        bottomBar = {
            Column {
                MiniPlayer(
                    modifier = Modifier
                        .zIndex(2f)
                        .padding(Dimens.Spacing08),
                    openPlaybackSheet = { navController.navigate(RootScreen.PlayerLibrary.route) }
                )

                HomeNavigationBar(
                    selectedTab = selectedTab,
                    onNavigationSelected = { selected ->
                        navController.selectRootScreen(selected)
                    },
                    isPlayerActive = isPlayerActive,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    ) { paddings ->
        ProvideScaffoldPadding(paddings) {
            AppNavigation(
                navController = navController,
                modifier = Modifier
            )
        }
    }
}

internal fun NavController.selectRootScreen(tab: RootScreen) {
    navigate(tab.route) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true

        val currentEntry = currentBackStackEntry
        val currentDestination = currentEntry?.destination
        val hostGraphRoute = currentDestination?.hostNavGraph?.route
        val isReselected = hostGraphRoute == tab.route
        val isRootReselected = currentDestination?.route == tab.startScreen.createRoute(RootScreen.PlayerLibrary)

        if (isReselected && !isRootReselected) {
            navigateUp()
        }
    }
}

internal val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph
