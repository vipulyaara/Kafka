package com.kafka.user.home.bottombar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.window.core.layout.WindowWidthSizeClass
import com.kafka.analytics.logger.Analytics
import com.kafka.common.adaptive.windowWidthSizeClass
import com.kafka.common.widgets.LocalSnackbarHostState
import com.kafka.navigation.currentScreenAsState
import com.kafka.navigation.graph.RootScreen
import com.kafka.navigation.graph.Screen
import com.kafka.navigation.graph.navigationRoute
import com.kafka.navigation.selectRootScreen
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.material.HazeScaffold
import com.kafka.ui.components.snackbar.DismissableSnackbarHost
import com.kafka.user.home.AppNavigation
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.artist
import com.sarahang.playback.core.isActive
import com.sarahang.playback.core.models.LocalPlaybackConnection
import com.sarahang.playback.ui.playback.speed.PlaybackSpeedViewModel
import com.sarahang.playback.ui.playback.timer.SleepTimerViewModel
import com.sarahang.playback.ui.player.mini.MiniPlayer
import com.sarahang.playback.ui.sheet.PlaybackArtworkPagerWithNowPlayingAndControls
import com.sarahang.playback.ui.sheet.PlaybackNowPlayingDefaults
import com.sarahang.playback.ui.sheet.rememberFlowWithLifecycle
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.shouldUseDarkColors


typealias HomeNavigation = @Composable (NavHostController, String) -> Unit

@Inject
@Composable
internal fun HomeNavigation(
    @Assisted navController: NavHostController,
    @Assisted playerTheme: String,
    analytics: Analytics,
    modifier: Modifier = Modifier,
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
    snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current,
    sleepTimerViewModelFactory: () -> SleepTimerViewModel,
    playbackSpeedViewModelFactory: () -> PlaybackSpeedViewModel,
    appNavigation: AppNavigation,
) {
    val selectedTab by navController.currentScreenAsState()
    val windowSizeClass = windowWidthSizeClass()
    val navigationType = remember(windowWidthSizeClass()) {
        NavigationType.forWindowSizeSize(windowSizeClass)
    }

    HazeScaffold(
        bottomBar = {
            if (shouldShowBottomBar(navController)) {
                HomeNavigationBar(
                    navigationType = navigationType,
                    selectedNavigation = selectedTab,
                    navigationItems = HomeNavigationItems,
                    playerTheme = playerTheme,
                    openPlaybackSheet = { navController.navigate(Screen.Player.route) },
                    onNavigationSelected = {
                        analytics.log {
                            homeTabSwitched(selectedTab.analyticsKey, "navigation_bar")
                        }
                        navController.selectRootScreen(it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        snackbarHost = { DismissableSnackbarHost(snackbarHostState) },
        blurBottomBar = navigationType == NavigationType.BOTTOM_NAVIGATION,
        modifier = modifier,
    ) { paddings ->
        ProvideScaffoldPadding(paddings) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                if (shouldShowBottomBar(navController)) {
                    if (navigationType == NavigationType.RAIL) {
                        HomeNavigationRail(
                            selectedNavigation = selectedTab,
                            navigationItems = HomeNavigationItems,
                            onNavigationSelected = {
                                analytics.log {
                                    homeTabSwitched(selectedTab.analyticsKey, "navigation_rail")
                                }
                                navController.selectRootScreen(it)
                            },
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(top = Dimens.Gutter),
                        )

                        VerticalDivider(color = MaterialTheme.colorScheme.surfaceContainer)
                    } else if (navigationType == NavigationType.PERMANENT_DRAWER) {
                        HomeNavigationDrawer(
                            selectedNavigation = selectedTab,
                            navigationItems = HomeNavigationItems,
                            onNavigationSelected = {
                                analytics.log {
                                    homeTabSwitched(selectedTab.analyticsKey, "navigation_drawer")
                                }
                                navController.selectRootScreen(it)
                            },
                            modifier = Modifier.fillMaxHeight(),
                        ) {
                            WideMiniPlayer(
                                playbackConnection = playbackConnection,
                                navController = navController,
                                sleepTimerViewModelFactory = sleepTimerViewModelFactory,
                                playbackSpeedViewModelFactory = playbackSpeedViewModelFactory
                            )
                        }

                        VerticalDivider(color = MaterialTheme.colorScheme.surfaceContainer)
                    }
                }

                appNavigation(navController)
            }
        }
    }
}

@Composable
private fun ColumnScope.WideMiniPlayer(
    playbackConnection: PlaybackConnection,
    navController: NavHostController,
    sleepTimerViewModelFactory: () -> SleepTimerViewModel,
    playbackSpeedViewModelFactory: () -> PlaybackSpeedViewModel
) {
    Spacer(Modifier.weight(1f))
    val playbackState by rememberFlowWithLifecycle(playbackConnection.playbackState)
    val nowPlaying by rememberFlowWithLifecycle(playbackConnection.nowPlaying)
    val visible = (playbackState to nowPlaying).isActive

    AnimatedVisibility(
        visible = visible,
        modifier = Modifier,
        enter = slideInVertically(initialOffsetY = { it / 2 }) + scaleIn(),
    ) {
        PlaybackArtworkPagerWithNowPlayingAndControls(
            nowPlaying = nowPlaying,
            playbackState = playbackState,
            isMiniPlayer = true,
            onArtworkClick = { navController.navigate(Screen.Player.route) },
            titleTextStyle = PlaybackNowPlayingDefaults.titleTextStyle
                .copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
            artistTextStyle = PlaybackNowPlayingDefaults.artistTextStyle.copy(
                fontSize = MaterialTheme.typography.titleSmall.fontSize
            ),
            onArtistClick = {
                navController.navigate(Screen.Search(nowPlaying.artist.orEmpty()))
            },
            sleepTimerViewModelFactory = sleepTimerViewModelFactory,
            playbackSpeedViewModelFactory = playbackSpeedViewModelFactory,
        )
    }
}

@Composable
private fun HomeNavigationBar(
    navigationType: NavigationType,
    selectedNavigation: RootScreen,
    navigationItems: List<HomeNavigationItem>,
    playerTheme: String,
    onNavigationSelected: (RootScreen) -> Unit,
    openPlaybackSheet: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        if (navigationType.shouldShowMiniPlayer()) {
            MiniPlayer(
                useDarkTheme = LocalTheme.current.shouldUseDarkColors(),
                modifier = Modifier
                    .padding(Dimens.Spacing08)
                    .zIndex(2f),
                openPlaybackSheet = openPlaybackSheet,
                playerTheme = playerTheme,
            )
        }

        if (navigationType == NavigationType.BOTTOM_NAVIGATION) {
            NavigationBar(
                containerColor = Color.Transparent,
                windowInsets = WindowInsets.navigationBars,
            ) {
                for (item in navigationItems) {
                    NavigationBarItem(
                        icon = {
                            HomeNavigationItemIcon(
                                item = item,
                                selected = selectedNavigation == item.rootScreen,
                            )
                        },
                        label = { Text(text = stringResource(item.labelResId)) },
                        selected = selectedNavigation == item.rootScreen,
                        onClick = { onNavigationSelected(item.rootScreen) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeNavigationRail(
    selectedNavigation: RootScreen,
    navigationItems: List<HomeNavigationItem>,
    onNavigationSelected: (RootScreen) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRail(modifier = modifier) {
        for (item in navigationItems) {
            NavigationRailItem(
                icon = {
                    HomeNavigationItemIcon(
                        item = item,
                        selected = selectedNavigation == item.rootScreen,
                    )
                },
                alwaysShowLabel = false,
                label = { Text(text = stringResource(item.labelResId)) },
                selected = selectedNavigation == item.rootScreen,
                onClick = { onNavigationSelected(item.rootScreen) },
            )
        }
    }
}

@Composable
private fun HomeNavigationDrawer(
    selectedNavigation: RootScreen,
    navigationItems: List<HomeNavigationItem>,
    onNavigationSelected: (RootScreen) -> Unit,
    modifier: Modifier = Modifier,
    extraContent: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .widthIn(max = 200.dp)
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
    ) {
        Column(
            modifier = Modifier
                .padding(Dimens.Spacing12)
                .background(MaterialTheme.colorScheme.surface)
                .width(IntrinsicSize.Max),
            verticalArrangement = Arrangement.spacedBy(Dimens.Spacing08)
        ) {
            for (item in navigationItems) {
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            imageVector = if (selectedNavigation == item.rootScreen) item.selectedImageVector else item.iconImageVector,
                            contentDescription = stringResource(item.contentDescriptionResId),
                        )
                    },
                    colors = NavigationDrawerItemDefaults.colors(
                        unselectedContainerColor = Color.Transparent,
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    label = {
                        Text(
                            text = stringResource(item.labelResId),
                            style = MaterialTheme.typography.titleSmall
                        )
                    },
                    selected = selectedNavigation == item.rootScreen,
                    onClick = { onNavigationSelected(item.rootScreen) },
                    shape = RectangleShape,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(Dimens.Radius08))
                )
            }
        }

        extraContent()
    }
}

@Composable
internal fun HomeNavigationItemIcon(item: HomeNavigationItem, selected: Boolean) {
    val painter = rememberVectorPainter(item.iconImageVector)
    val selectedPainter = rememberVectorPainter(item.selectedImageVector)

    Icon(
        painter = if (selected) selectedPainter else painter,
        contentDescription = stringResource(item.contentDescriptionResId),
    )
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

internal enum class NavigationType {
    BOTTOM_NAVIGATION,
    RAIL,
    PERMANENT_DRAWER;

    fun shouldShowMiniPlayer(): Boolean = this == BOTTOM_NAVIGATION || this == RAIL

    companion object {
        fun forWindowSizeSize(
            windowSizeClass: WindowWidthSizeClass
        ): NavigationType = when (windowSizeClass) {
            WindowWidthSizeClass.COMPACT -> BOTTOM_NAVIGATION
            WindowWidthSizeClass.MEDIUM -> RAIL
            else -> PERMANENT_DRAWER
        }
    }
}
