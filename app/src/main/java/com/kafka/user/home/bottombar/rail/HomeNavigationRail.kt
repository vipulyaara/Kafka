/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.kafka.user.home.bottombar.rail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.user.home.bottombar.HomeNavigationItemIcon
import com.kafka.user.home.bottombar.HomeNavigationItems
import com.kafka.user.home.bottombar.rail.HomeNavigationRailDefaults.ExpandedPlaybackControlsMinWidth
import com.kafka.user.home.bottombar.rail.HomeNavigationRailDefaults.ExpandedPlaybackModeMinHeight
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.isActive
import com.sarahang.playback.core.models.LocalPlaybackConnection
import com.sarahang.playback.ui.player.mini.MiniPlayer
import com.sarahang.playback.ui.sheet.PlaybackArtworkPagerWithNowPlayingAndControls
import com.sarahang.playback.ui.sheet.PlaybackNowPlayingDefaults
import com.sarahang.playback.ui.sheet.rememberFlowWithLifecycle
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Navigator
import org.kafka.navigation.RootScreen
import org.kafka.navigation.Screen
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.shouldUseDarkColors

internal object HomeNavigationRailDefaults {
    val ActiveColor @Composable get() = MaterialTheme.colorScheme.primary
    val OnActiveColor @Composable get() = MaterialTheme.colorScheme.onSecondary
    val OnInactiveColor @Composable get() = MaterialTheme.colorScheme.onSurface

    val windowInsets: WindowInsets
        @Composable
        get() = WindowInsets.systemBars
            .only(WindowInsetsSides.Vertical + WindowInsetsSides.Start)

    val colors
        @Composable
        get() = NavigationRailItemDefaults.colors(
            indicatorColor = ActiveColor,
            selectedTextColor = ActiveColor,
            selectedIconColor = OnActiveColor,
            unselectedIconColor = OnInactiveColor,
            unselectedTextColor = OnInactiveColor,
        )

    val ExpandedNavigationItemMinWidth = 100.dp
    val ExpandedPlaybackControlsMinWidth = 200.dp
    val ExpandedPlaybackModeMinHeight = 600.dp
}

@Composable
internal fun HomeNavigationRail(
    selectedTab: RootScreen,
    onNavigationSelected: (RootScreen) -> Unit,
    onPlayingArtistClick: () -> Unit,
    modifier: Modifier = Modifier,
    extraContent: @Composable BoxScope.() -> Unit = {},
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current,
    navigator: Navigator = LocalNavigator.current,
) {
    val currentRoot by navigator.currentRoot.collectAsStateWithLifecycle()
    Surface(
        tonalElevation = 0.dp,
        shadowElevation = 8.dp,
        modifier = modifier,
    ) {
        BoxWithConstraints {
            extraContent()
            val maxWidth = maxWidth
            val isExpandedPlaybackControls =
                maxWidth > ExpandedPlaybackControlsMinWidth && maxHeight > ExpandedPlaybackModeMinHeight
            val isExpandedNavigationItem =
                maxWidth > HomeNavigationRailDefaults.ExpandedNavigationItemMinWidth

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .windowInsetsPadding(HomeNavigationRailDefaults.windowInsets),
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Start)
                        .selectableGroup()
                        .verticalScroll(rememberScrollState())
                        .weight(4f)
                        .padding(top = Dimens.Spacing24, bottom = Dimens.Spacing24)
                ) {
                    HomeNavigationItems.forEach { item ->
                        val isSelected = selectedTab == item.rootScreen

                        if (isExpandedNavigationItem) {
                            HomeNavigationRailItemRow(
                                item = item,
                                selected = isSelected,
                                onClick = { onNavigationSelected(item.rootScreen) },
                            )
                        } else {
                            NavigationRailItem(
                                selected = isSelected,
                                onClick = { onNavigationSelected(item.rootScreen) },
                                icon = {
                                    HomeNavigationItemIcon(item = item, selected = isSelected)
                                },
                                label = {
                                    Text(
                                        text = stringResource(item.labelResId),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                },
                                alwaysShowLabel = false,
                                colors = HomeNavigationRailDefaults.colors,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = Dimens.Spacing32,
                                        vertical = Dimens.Spacing24
                                    )
                                    .align(Alignment.CenterHorizontally),
                            )
                        }
                    }
                }

                if (isExpandedPlaybackControls) {
                    val expandedPlaybackControlsWeight =
                        3f + ((maxWidth - ExpandedPlaybackControlsMinWidth) / ExpandedPlaybackControlsMinWidth * 2.5f)
                    val playbackState by rememberFlowWithLifecycle(playbackConnection.playbackState)
                    val nowPlaying by rememberFlowWithLifecycle(playbackConnection.nowPlaying)
                    val visible = (playbackState to nowPlaying).isActive
                    AnimatedVisibility(
                        visible = visible,
                        modifier = Modifier.weight(expandedPlaybackControlsWeight),
                        enter = slideInVertically(initialOffsetY = { it / 2 }) + scaleIn(),
                    ) {
                        PlaybackArtworkPagerWithNowPlayingAndControls(
                            nowPlaying = nowPlaying,
                            playbackState = playbackState,
                            onArtworkClick = {
                                navigator.navigate(Screen.Player.createRoute(currentRoot))
                            },
                            titleTextStyle = PlaybackNowPlayingDefaults.titleTextStyle.copy(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                            artistTextStyle = PlaybackNowPlayingDefaults.artistTextStyle.copy(
                                fontSize = MaterialTheme.typography.titleSmall.fontSize
                            ),
                            onArtistClick = onPlayingArtistClick,
                        )
                    }
                } else {
                    MiniPlayer(
                        modifier = Modifier
                            .padding(Dimens.Spacing08)
                            .zIndex(2f),
                        useDarkTheme = LocalTheme.current.shouldUseDarkColors(),
                        openPlaybackSheet = {
                            navigator.navigate(Screen.Player.createRoute(currentRoot))
                        }
                    )
                }
            }
        }
    }
}
