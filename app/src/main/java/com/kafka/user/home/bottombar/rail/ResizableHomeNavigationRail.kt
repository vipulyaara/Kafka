/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.kafka.user.home.bottombar.rail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sarahang.playback.ui.components.ResizableLayout
import com.sarahang.playback.ui.sheet.rememberFlowWithLifecycle
import org.kafka.analytics.logger.Analytics
import org.kafka.navigation.graph.RootScreen
import org.kafka.navigation.selectRootScreen

@Composable
internal fun RowScope.ResizableHomeNavigationRail(
    availableWidth: Dp,
    selectedTab: RootScreen,
    navController: NavHostController,
    analytics: Analytics,
    baseWeight: Float = 4.5f,
    minWeight: Float = 1f,
    maxWeight: Float = 12f,
    viewModel: ResizableHomeNavigationRailViewModel = hiltViewModel(),
    dragOffset: State<Float> = rememberFlowWithLifecycle(viewModel.dragOffset),
    setDragOffset: (Float) -> Unit = viewModel::setDragOffset,
    onPlayingArtistClick: () -> Unit,
) {
    ResizableLayout(
        availableWidth = availableWidth,
        initialWeight = baseWeight,
        minWeight = minWeight,
        maxWeight = maxWeight,
        dragOffset = dragOffset,
        setDragOffset = setDragOffset,
    ) { resizableModifier ->
        HomeNavigationRail(
            selectedTab = selectedTab,
            onNavigationSelected = { selected ->
                analytics.log { homeTabSwitched(selected.analyticsKey, "navigation_rail") }
                navController.selectRootScreen(selected)
            },
            onPlayingArtistClick = onPlayingArtistClick,
            modifier = Modifier
                .fillMaxHeight()
                .then(resizableModifier),
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterEnd)
        ) {
            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
            )
        }
    }
}
