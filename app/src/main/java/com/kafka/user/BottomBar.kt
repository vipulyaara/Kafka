package com.kafka.user

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import org.kafka.common.image.Icons
import org.kafka.navigation.RootScreen
import org.kafka.user.R

internal val HomeNavigationItems = listOf(
    HomeNavigationItem.ImageVectorIcon(
        rootScreen = RootScreen.Home,
        labelResId = R.string.home,
        contentDescriptionResId = R.string.home,
        iconImageVector = Icons.Home,
        selectedImageVector = Icons.HomeActive,
    ),
    HomeNavigationItem.ImageVectorIcon(
        rootScreen = RootScreen.Search,
        labelResId = R.string.bottom_bar_search,
        contentDescriptionResId = R.string.bottom_bar_search,
        iconImageVector = Icons.Search,
        selectedImageVector = Icons.SearchActive,
    ),
//    HomeNavigationItem.ImageVectorIcon(
//        screen = Screen.PlayerLibrary,
//        labelResId = R.string.player_library,
//        contentDescriptionResId = R.string.player_library,
//        iconImageVector = Icons.PlayCircle,
//    ),
    HomeNavigationItem.ImageVectorIcon(
        rootScreen = RootScreen.Library,
        labelResId = R.string.library,
        contentDescriptionResId = R.string.library,
        iconImageVector = Icons.Library,
        selectedImageVector = Icons.LibraryActive,
    ),
//    HomeNavigationItem.ImageVectorIcon(
//        rootScreen = RootScreen.Profile,
//        labelResId = R.string.bottom_bar_profile,
//        contentDescriptionResId = R.string.bottom_bar_profile,
//        iconImageVector = Icons.Profile,
//        selectedImageVector = Icons.ProfileActive,
//    ),
)

internal sealed class HomeNavigationItem(
    val rootScreen: RootScreen,
    @StringRes val labelResId: Int,
    @StringRes val contentDescriptionResId: Int,
) {
    class ImageVectorIcon(
        rootScreen: RootScreen,
        @StringRes labelResId: Int,
        @StringRes contentDescriptionResId: Int,
        val iconImageVector: ImageVector,
        val selectedImageVector: ImageVector? = null,
    ) : HomeNavigationItem(rootScreen, labelResId, contentDescriptionResId)
}
