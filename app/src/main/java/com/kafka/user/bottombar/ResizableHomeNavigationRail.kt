/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.kafka.user.bottombar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.sarahang.playback.ui.components.ResizableLayout
import com.sarahang.playback.ui.sheet.rememberFlowWithLifecycle
import org.kafka.navigation.RootScreen
import org.kafka.navigation.selectRootScreen

@Composable
internal fun RowScope.ResizableHomeNavigationRail(
    availableWidth: Dp,
    selectedTab: RootScreen,
    navController: NavHostController,
    baseWeight: Float = 4.5f,
    minWeight: Float = 1f,
    maxWeight: Float = 12f,
    viewModel: ResizableHomeNavigationRailViewModel = hiltViewModel(),
    dragOffset: State<Float> = rememberFlowWithLifecycle(viewModel.dragOffset),
    setDragOffset: (Float) -> Unit = viewModel::setDragOffset,
    onPlayingTitleClick: () -> Unit,
    onPlayingArtistClick: () -> Unit,
) {
    ResizableLayout(
        availableWidth = availableWidth,
        initialWeight = baseWeight,
        minWeight = minWeight,
        maxWeight = maxWeight,
        dragOffset = dragOffset,
        setDragOffset = setDragOffset,
        analyticsPrefix = "home.navigationRail",
    ) { resizableModifier ->
        HomeNavigationRail(
            selectedTab = selectedTab,
            onNavigationSelected = { selected -> navController.selectRootScreen(selected) },
            onPlayingTitleClick = onPlayingTitleClick,
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
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
            )
        }
    }
}
