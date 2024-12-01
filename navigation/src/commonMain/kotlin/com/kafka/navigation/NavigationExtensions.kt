package com.kafka.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.kafka.navigation.graph.RootScreen
import com.kafka.navigation.graph.navigationRoute

fun NavController.selectRootScreen(tab: RootScreen) {
    navigate(tab) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true

        val currentEntry = currentBackStackEntry
        val currentDestination = currentEntry?.destination
        val hostGraphRoute = currentDestination?.hostNavGraph?.route
        val isReselected = hostGraphRoute == tab.navigationRoute
        if (isReselected) {
            navigateUp()
        }
    }
}

@Stable
@Composable
fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Home) }
    val rootScreens = listOf(RootScreen.Home, RootScreen.Search, RootScreen.Library, RootScreen.Profile)
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            rootScreens.firstOrNull { rs -> destination.hierarchy.any { it.route == rs.navigationRoute } }
                ?.let { selectedItem.value = it }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

internal val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph
