package com.kafka.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.kafka.navigation.graph.RootScreen
import com.kafka.navigation.graph.Screen
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import me.tatarka.inject.annotations.Inject

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

@Inject
class NavigatorImpl : Navigator {
    private val navigationQueue = Channel<NavigationEvent>(Channel.CONFLATED)
    override val queue = navigationQueue.receiveAsFlow()

    override fun navigate(route: Screen, root: RootScreen?) {
        navigationQueue.trySend(NavigationEvent.Destination(route, root))
    }

    override fun goBack() {
        navigationQueue.trySend(NavigationEvent.Back)
    }
}
