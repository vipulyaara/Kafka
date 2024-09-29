package org.kafka.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import org.kafka.navigation.graph.RootScreen
import org.kafka.navigation.graph.Screen
import org.kafka.navigation.graph.navigationRoute
import javax.inject.Inject

interface Navigator {
    fun navigate(route: Screen, root: RootScreen? = null)
    fun goBack()
    val queue: Flow<NavigationEvent>
}

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("No LocalNavigator given")
}

@Composable
fun NavigatorHost(
    navigator: Navigator,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalNavigator provides navigator, content = content)
}

sealed class NavigationEvent(open val route: Screen) {
    data object Back : NavigationEvent(Screen.Back)
    data class Destination(
        override val route: Screen,
        val root: RootScreen? = null,
    ) : NavigationEvent(route)
}

class NavigatorImpl @Inject constructor() : Navigator {
    private val navigationQueue = Channel<NavigationEvent>(Channel.CONFLATED)
    override val queue = navigationQueue.receiveAsFlow()

    override fun navigate(route: Screen, root: RootScreen?) {
        navigationQueue.trySend(NavigationEvent.Destination(route, root))
    }

    override fun goBack() {
        navigationQueue.trySend(NavigationEvent.Back)
    }
}

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
    val rootScreens = ROOT_SCREENS
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

val ROOT_SCREENS =
    listOf(RootScreen.Home, RootScreen.Search, RootScreen.Library, RootScreen.Profile)

internal val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph
