package com.kafka.shared.home.bottombar

import androidx.compose.ui.graphics.vector.ImageVector
import com.kafka.common.image.Icons
import com.kafka.navigation.graph.RootScreen
import kafka.ui.shared.generated.resources.Res
import kafka.ui.shared.generated.resources.bottom_bar_search
import kafka.ui.shared.generated.resources.home
import kafka.ui.shared.generated.resources.library
import org.jetbrains.compose.resources.StringResource

internal val HomeNavigationItems = listOf(
    HomeNavigationItem(
        rootScreen = RootScreen.Home,
        labelResId = Res.string.home,
        contentDescriptionResId = Res.string.home,
        iconImageVector = Icons.Home,
        selectedImageVector = Icons.HomeActive,
    ),
    HomeNavigationItem(
        rootScreen = RootScreen.Search,
        labelResId = Res.string.bottom_bar_search,
        contentDescriptionResId = Res.string.bottom_bar_search,
        iconImageVector = Icons.Search,
        selectedImageVector = Icons.SearchActive,
    ),
    HomeNavigationItem(
        rootScreen = RootScreen.Library,
        labelResId = Res.string.library,
        contentDescriptionResId = Res.string.library,
        iconImageVector = Icons.Library,
        selectedImageVector = Icons.LibraryActive,
    )
)

internal data class HomeNavigationItem(
    val rootScreen: RootScreen,
    val labelResId: StringResource,
    val contentDescriptionResId: StringResource,
    val iconImageVector: ImageVector,
    val selectedImageVector: ImageVector,
)