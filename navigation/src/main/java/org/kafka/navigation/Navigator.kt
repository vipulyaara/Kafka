package org.kafka.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.kafka.base.debug
import org.kafka.base.errorLog

val LocalNavigator = staticCompositionLocalOf<Navigator> {
    error("No LocalNavigator given")
}

@Composable
fun NavigatorHost(
    viewModel: NavigatorViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalNavigator provides viewModel.navigator, content = content)
}

sealed class NavigationEvent(
    open val route: String,
    open val builder: NavOptionsBuilder.() -> Unit
) {
    object Back : NavigationEvent("Back", {})
    data class Destination(
        override val route: String,
        override val builder: NavOptionsBuilder.() -> Unit
    ) : NavigationEvent(route, builder)
}

class Navigator {
    private val navigationQueue = Channel<NavigationEvent>(Channel.CONFLATED)
    var navController: NavController? = null

    val currentBundle
        get() = navController?.currentBackStackEntry?.arguments

    private val lastRoot
        get() = navController?.currentBackStackEntry?.destination?.parent?.route

    fun navigate(
        route: String,
        builder: NavOptionsBuilder.() -> Unit = {}
    ) {
        val routeWithRoot = if (route.contains("root")) route else "${lastRoot}/$route"
        debug {
            "Navigating to $routeWithRoot with navigation queue size ${navController}"
        }
        val result = navigationQueue.trySend(NavigationEvent.Destination(routeWithRoot, builder))
        if (result.isFailure) errorLog { "Navigation result is $result $routeWithRoot" }
    }

    fun back() {
        navigationQueue.trySend(NavigationEvent.Back)
    }

    fun attachNavController(navController: NavController) {
        this.navController = navController
    }

    val queue = navigationQueue.receiveAsFlow()
}
