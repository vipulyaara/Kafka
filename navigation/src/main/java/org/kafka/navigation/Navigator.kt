package org.kafka.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.kafka.base.debug

interface Navigator {
    fun navigate(route: String)
    fun goBack()
    fun updateRoot(root: RootScreen)
    val queue: Flow<NavigationEvent>
    val currentRoot: StateFlow<RootScreen>
}

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("No LocalNavigator given")
}

@Composable
fun NavigatorHost(content: @Composable () -> Unit) {
    NavigatorHost(
        navigator = hiltViewModel<NavigatorViewModel>().navigator,
        content = content,
    )
}

@Composable
private fun NavigatorHost(
    navigator: Navigator,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalNavigator provides navigator, content = content)
}

sealed class NavigationEvent(open val route: String) {
    object Back : NavigationEvent("Back")
    data class Destination(override val route: String, val root: RootScreen? = null) :
        NavigationEvent(route)

    override fun toString() = route
}

class NavigatorImpl : Navigator {
    private val navigationQueue = Channel<NavigationEvent>(Channel.CONFLATED)
    private val currentRootChannel = MutableStateFlow<RootScreen>(RootScreen.Home)
    override val queue = navigationQueue.receiveAsFlow()
    override val currentRoot = currentRootChannel

    override fun navigate(route: String) {
        debug { "Navigating to $route" }

        val rootPath = route.split("/").firstOrNull()
        val rootScreen = ROOT_SCREENS.firstOrNull { it.route == rootPath }

        debug { "Navigating to $route with root $rootScreen" }
        navigationQueue.trySend(NavigationEvent.Destination(route, rootScreen))
    }

    override fun updateRoot(root: RootScreen) {
        currentRootChannel.tryEmit(root)
    }

    override fun goBack() {
        navigationQueue.trySend(NavigationEvent.Back)
    }
}

fun NavController.selectRootScreen(tab: RootScreen) {
    debug { "Selecting root screen $tab" }
    navigate(tab.route) {
        launchSingleTop = true
        restoreState = true

        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }

        val currentEntry = currentBackStackEntry
        val currentDestination = currentEntry?.destination
        val hostGraphRoute = currentDestination?.hostNavGraph?.route
        val isReselected = hostGraphRoute == tab.route
        if (isReselected) {
            navigateUp()
        }
    }
}

val ROOT_SCREENS =
    listOf(RootScreen.Home, RootScreen.Search, RootScreen.Library, RootScreen.Profile)

internal val NavDestination.hostNavGraph: NavGraph
    get() = hierarchy.first { it is NavGraph } as NavGraph
