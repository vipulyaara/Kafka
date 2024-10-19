package com.kafka.user.home.bottombar

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.kafka.user.R
import com.kafka.common.image.Icons
import com.kafka.navigation.graph.RootScreen

internal val HomeNavigationItems = listOf(
    HomeNavigationItem(
        rootScreen = RootScreen.Home,
        labelResId = R.string.home,
        contentDescriptionResId = R.string.home,
        iconImageVector = Icons.Home,
        selectedImageVector = Icons.HomeActive,
    ),
    HomeNavigationItem(
        rootScreen = RootScreen.Search,
        labelResId = R.string.bottom_bar_search,
        contentDescriptionResId = R.string.bottom_bar_search,
        iconImageVector = Icons.Search,
        selectedImageVector = Icons.SearchActive,
    ),
    HomeNavigationItem(
        rootScreen = RootScreen.Library,
        labelResId = R.string.library,
        contentDescriptionResId = R.string.library,
        iconImageVector = Icons.Library,
        selectedImageVector = Icons.LibraryActive,
    )
)

internal data class HomeNavigationItem(
    val rootScreen: RootScreen,
    @StringRes val labelResId: Int,
    @StringRes val contentDescriptionResId: Int,
    val iconImageVector: ImageVector,
    val selectedImageVector: ImageVector,
)
