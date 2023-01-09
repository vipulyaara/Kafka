package com.kafka.user

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import org.kafka.common.Icons
import org.kafka.navigation.RootScreen
import org.kafka.user.R
import ui.common.theme.theme.translucentSurfaceColor

@Composable
fun RekhtaBottomBar(navController: NavController) {
    val currentSelectedItem by navController.currentScreenAsState()

    NavigationBar(
        containerColor = translucentSurfaceColor(),
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface),
        windowInsets = WindowInsets.navigationBars,
        modifier = Modifier.fillMaxWidth()
    ) {
        HomeNavigationItems.forEach { item ->
                NavigationBarItem(
                    icon = {
                        HomeNavigationItemIcon(
                            item = item,
                            selected = currentSelectedItem == item.rootScreen
                        )
                    },
                    alwaysShowLabel = false,
                    selected = currentSelectedItem == item.rootScreen,
                    onClick = {
                        navController.navigate(item.rootScreen.route) {
                            launchSingleTop = true
                            restoreState = true

                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                        }
                    },
                )
            }
        }
}

@Composable
private fun HomeNavigationItemIcon(item: HomeNavigationItem, selected: Boolean) {
    val painter = when (item) {
        is HomeNavigationItem.ImageVectorIcon -> rememberVectorPainter(item.iconImageVector)
    }
    val selectedPainter = item.selectedImageVector?.let { rememberVectorPainter(it) }

    if (selectedPainter != null) {
        Crossfade(targetState = selected) {
            Icon(
                painter = if (it) selectedPainter else painter,
                contentDescription = stringResource(id = item.contentDescriptionResId),
            )
        }
    } else {
        Icon(
            painter = painter,
            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            contentDescription = stringResource(id = item.contentDescriptionResId),
        )
    }
}

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
    HomeNavigationItem.ImageVectorIcon(
        rootScreen = RootScreen.Profile,
        labelResId = R.string.bottom_bar_profile,
        contentDescriptionResId = R.string.bottom_bar_profile,
        iconImageVector = Icons.Profile,
        selectedImageVector = Icons.ProfileActive,
    ),
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
