package com.kafka.user

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import org.kafka.common.Icons
import org.kafka.navigation.Screen
import org.kafka.user.R
import ui.common.theme.BottomBarElevation
import ui.common.theme.theme.AppBarAlphas
import ui.common.theme.theme.DisableRipple
import ui.common.theme.theme.ProvideRipple
import ui.common.theme.theme.textPrimary

@Composable
fun RekhtaBottomBar(navController: NavController) {
    val currentSelectedItem by navController.currentScreenAsState()

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = AppBarAlphas.translucentBarAlpha()),
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        HomeNavigationItems.forEach { item ->
            DisableRipple {
                NavigationBarItem(
                    icon = {
                        HomeNavigationItemIcon(
                            item = item,
                            selected = currentSelectedItem == item.screen
                        )
                    },
                    label = {
                        Text(
                            text = stringResource(item.labelResId),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    alwaysShowLabel = false,
                    selected = currentSelectedItem == item.screen,
                    onClick = {
                        navController.navigate(item.screen.route) {
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
}

@Composable
internal fun HomeBottomNavigation(
    selectedNavigation: Screen,
    onNavigationSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier,
    openLanguageMenu: () -> Unit
) {
    NavigationBar(
        tonalElevation = BottomBarElevation,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.height(56.dp)
    ) {
        HomeNavigationItems.forEach { item ->
            val selected = selectedNavigation == item.screen
            HomeNavigationItem(
                modifier = Modifier.align(Alignment.CenterVertically),
                icon = { HomeNavigationItemIcon(item = item, selected = selected) },
                label = {
                    Text(
                        text = stringResource(id = item.labelResId),
                        style = MaterialTheme.typography.titleSmall,
                        color = if (selected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.textPrimary
                    )
                },
                selected = selected,
                onClick = { onNavigationSelected(item.screen) },
                onDoubleClick = {
                    if (item.screen == Screen.Profile) {
                        openLanguageMenu()
                    }
                }
            )
        }
    }
}

@Composable
fun HomeNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onDoubleClick: () -> Unit = {},
    label: @Composable () -> Unit
) {
    ProvideRipple(isBounded = false) {
        Card(
            modifier = modifier.padding(vertical = 8.dp),
            colors = cardColors(containerColor = MaterialTheme.colorScheme.background),
            shape = RoundedCornerShape(50)
        ) {
            Column(
                modifier = Modifier
                    .animateContentSize()
                    .combinedClickable(
                        onClick = onClick,
                        onDoubleClick = onDoubleClick
                    )
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!selected) {
                    icon()
                }
                if (selected) {
                    label.invoke()
                }
                if (selected) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }
            }
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

@Stable
@Composable
private fun NavController.currentScreenAsState(): State<Screen> {
    val selectedItem = remember { mutableStateOf<Screen>(Screen.Home) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Screen.Home.route } -> {
                    selectedItem.value = Screen.Home
                }
                destination.hierarchy.any { it.route == Screen.Search.route } -> {
                    selectedItem.value = Screen.Search
                }
                destination.hierarchy.any { it.route == Screen.PlayerLibrary.route } -> {
                    selectedItem.value = Screen.PlayerLibrary
                }
                destination.hierarchy.any { it.route == Screen.Library.route } -> {
                    selectedItem.value = Screen.Library
                }
                destination.hierarchy.any { it.route == Screen.Profile.route } -> {
                    selectedItem.value = Screen.Profile
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

private val HomeNavigationItems = listOf(
    HomeNavigationItem.ImageVectorIcon(
        screen = Screen.Home,
        labelResId = R.string.home,
        contentDescriptionResId = R.string.home,
        iconImageVector = Icons.Home,
        selectedImageVector = Icons.HomeActive,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = Screen.Search,
        labelResId = R.string.search,
        contentDescriptionResId = R.string.search,
        iconImageVector = Icons.Search,
        selectedImageVector = Icons.SearchActive,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = Screen.PlayerLibrary,
        labelResId = R.string.player_library,
        contentDescriptionResId = R.string.player_library,
        iconImageVector = Icons.PlayCircle,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = Screen.Library,
        labelResId = R.string.library,
        contentDescriptionResId = R.string.library,
        iconImageVector = Icons.Library,
        selectedImageVector = Icons.LibraryActive,
    ),
    HomeNavigationItem.ImageVectorIcon(
        screen = Screen.Profile,
        labelResId = R.string.profile,
        contentDescriptionResId = R.string.profile,
        iconImageVector = Icons.Profile,
        selectedImageVector = Icons.ProfileActive,
    ),
)

internal sealed class HomeNavigationItem(
    val screen: Screen,
    @StringRes val labelResId: Int,
    @StringRes val contentDescriptionResId: Int,
) {
    class ImageVectorIcon(
        screen: Screen,
        @StringRes labelResId: Int,
        @StringRes contentDescriptionResId: Int,
        val iconImageVector: ImageVector,
        val selectedImageVector: ImageVector? = null,
    ) : HomeNavigationItem(screen, labelResId, contentDescriptionResId)
}
