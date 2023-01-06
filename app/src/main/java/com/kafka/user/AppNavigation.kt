package com.kafka.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.kafka.reader.ReaderScreen
import com.kafka.search.SearchScreen
import org.kafka.common.extensions.CollectEvent
import org.kafka.favorites.FavoriteScreen
import org.kafka.homepage.Homepage
import org.kafka.item.detail.ItemDetail
import org.kafka.item.files.Files
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.NavigationEvent
import org.kafka.navigation.Navigator
import org.kafka.navigation.ROOT_SCREENS
import org.kafka.navigation.RootScreen
import org.kafka.navigation.composableScreen
import org.kafka.ui.components.defaultEnterTransition
import org.kafka.ui.components.defaultExitTransition
import org.kafka.ui.components.defaultPopEnterTransition
import org.kafka.ui.components.defaultPopExitTransition
import org.kafka.webview.WebView

@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    navigator: Navigator = LocalNavigator.current
) {
    CollectEvent(navigator.queue) { event ->
        when (event) {
            is NavigationEvent.Destination -> {
                // switch tabs first because of a bug in navigation that doesn't allow
                // changing tabs when destination is opened from a different tab
                event.root?.let {
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                navController.navigate(event.route)
            }

            is NavigationEvent.Back -> navController.navigateUp()
            else -> Unit
        }
    }

    AnimatedNavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = RootScreen.Home.route,
        enterTransition = { defaultEnterTransition(initialState, targetState) },
        exitTransition = { defaultExitTransition(initialState, targetState) },
        popEnterTransition = { defaultPopEnterTransition() },
        popExitTransition = { defaultPopExitTransition() }
    ) {
        addHomeRoot()
        addSearchRoot()
        addPlayerLibraryRoot()
        addLibraryRoot()
        addProfileRoot()
    }
}

private fun NavGraphBuilder.addHomeRoot() {
    navigation(
        route = RootScreen.Home.route,
        startDestination = LeafScreen.Home.createRoute(RootScreen.Home)
    ) {
        addHome(RootScreen.Home)
        addItemDetail(RootScreen.Home)
        addFiles(RootScreen.Home)
        addReader(RootScreen.Home)
        addWebView(RootScreen.Home)
    }
}

private fun NavGraphBuilder.addSearchRoot() {
    navigation(
        route = RootScreen.Search.route,
        startDestination = LeafScreen.Search.createRoute(RootScreen.Search)
    ) {
        addSearch(RootScreen.Search)
        addItemDetail(RootScreen.Search)
        addFiles(RootScreen.Search)
        addReader(RootScreen.Search)
        addWebView(RootScreen.Search)
    }
}

private fun NavGraphBuilder.addLibraryRoot() {
    navigation(
        route = RootScreen.Library.route,
        startDestination = LeafScreen.Library.createRoute(RootScreen.Library)
    ) {
        addLibrary(RootScreen.Library)
        addItemDetail(RootScreen.Library)
        addFiles(RootScreen.Library)
        addReader(RootScreen.Library)
    }
}

private fun NavGraphBuilder.addPlayerLibraryRoot() {
    navigation(
        route = RootScreen.PlayerLibrary.route,
        startDestination = LeafScreen.PlayerLibrary.createRoute(RootScreen.PlayerLibrary)
    ) {
        addPlayer(RootScreen.PlayerLibrary)
    }
}

private fun NavGraphBuilder.addProfileRoot() {
    navigation(
        route = RootScreen.Profile.route,
        startDestination = LeafScreen.Profile.createRoute(RootScreen.Profile)
    ) {
        addProfile(RootScreen.Profile)
    }
}

private fun NavGraphBuilder.addHome(root: RootScreen) {
    composable(LeafScreen.Home.createRoute(root)) {
        Homepage()
    }
}

private fun NavGraphBuilder.addSearch(root: RootScreen) {
    composable(LeafScreen.Search.createRoute(root)) {
        SearchScreen()
    }
}

private fun NavGraphBuilder.addPlayer(root: RootScreen) {
    composable(LeafScreen.PlayerLibrary.createRoute(root)) {

    }
}

private fun NavGraphBuilder.addLibrary(root: RootScreen) {
    composable(LeafScreen.Library.createRoute(root)) {
        FavoriteScreen()
    }
}

private fun NavGraphBuilder.addProfile(root: RootScreen) {
    composable(LeafScreen.Profile.createRoute(root)) {

    }
}

private fun NavGraphBuilder.addItemDetail(root: RootScreen) {
    composableScreen(LeafScreen.ItemDetail(rootRoute = root.route)) {
        ItemDetail()
    }
}

private fun NavGraphBuilder.addFiles(root: RootScreen) {
    composableScreen(LeafScreen.Files(rootRoute = root.route)) {
        Files()
    }
}

private fun NavGraphBuilder.addReader(root: RootScreen) {
    composableScreen(LeafScreen.Reader(rootRoute = root.route)) {
        ReaderScreen()
    }
}

private fun NavGraphBuilder.addWebView(root: RootScreen) {
    composable(
        route = LeafScreen.WebView.createRoute(root),
        arguments = listOf(
            navArgument("url") { type = NavType.StringType }
        )
    ) {
        WebView(it.arguments?.getString("url").orEmpty())
    }
}

@Stable
@Composable
internal fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Search) }
    val rootScreens = ROOT_SCREENS
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            rootScreens.firstOrNull { rs -> destination.hierarchy.any { it.route == rs.route } }?.let {
                selectedItem.value = it
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}
