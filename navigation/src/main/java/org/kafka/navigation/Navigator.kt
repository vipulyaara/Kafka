package org.kafka.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.kafka.base.debug

interface Navigator {
    fun navigate(route: String)
    fun goBack()
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
    data class Destination(override val route: String, val root: String? = null) :
        NavigationEvent(route)

    override fun toString() = route
}

class NavigatorImpl : Navigator {
    private val navigationQueue = Channel<NavigationEvent>(Channel.CONFLATED)
    private val currentRootChannel = MutableStateFlow<RootScreen>(RootScreen.Home)
    override val queue = navigationQueue.receiveAsFlow()
    override val currentRoot = currentRootChannel

    override fun navigate(route: String) {
        val basePath = route.split("/").firstOrNull()
        val rootScreen = ROOT_SCREENS.firstOrNull { it.route == basePath }
        val root = if (rootScreen != null) basePath else null

        if (rootScreen != null) {
            currentRootChannel.tryEmit(rootScreen)
        }

        debug { "Navigating to $route with root $root" }
        navigationQueue.trySend(NavigationEvent.Destination(route, root))
    }

    override fun goBack() {
        navigationQueue.trySend(NavigationEvent.Back)
    }
}
