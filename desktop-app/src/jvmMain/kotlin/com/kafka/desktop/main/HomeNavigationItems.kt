package com.kafka.desktop.main

import androidx.compose.ui.graphics.vector.ImageVector
import com.kafka.common.image.Icons
import com.kafka.navigation.graph.RootScreen

internal val HomeNavigationItems = listOf(
    HomeNavigationItem(
        rootScreen = RootScreen.Home,
        labelResId = "Home",
        iconImageVector = Icons.Home,
        selectedImageVector = Icons.HomeActive,
    ),
    HomeNavigationItem(
        rootScreen = RootScreen.Search,
        labelResId = "Search",
        iconImageVector = Icons.Search,
        selectedImageVector = Icons.SearchActive,
    ),
    HomeNavigationItem(
        rootScreen = RootScreen.Library,
        labelResId = "Library",
        iconImageVector = Icons.Library,
        selectedImageVector = Icons.LibraryActive,
    )
)

internal data class HomeNavigationItem(
    val rootScreen: RootScreen,
   val  labelResId: String,
    val iconImageVector: ImageVector,
    val selectedImageVector: ImageVector,
)
