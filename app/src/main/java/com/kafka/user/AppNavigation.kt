package com.kafka.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import org.kafka.navigation.*
import org.kafka.ui.components.defaultEnterTransition
import org.kafka.ui.components.defaultExitTransition
import org.kafka.ui.components.defaultPopEnterTransition
import org.kafka.ui.components.defaultPopExitTransition
import org.kafka.webview.WebView
import timber.log.Timber

@Composable
internal fun AppNavigation(
    navController: NavHostController,
    navigator: Navigator = LocalNavigator.current
) {
    navigator.attachNavController(navController)
    CollectEvent(navigator.queue) { event ->
        Timber.i("Navigation event: $event")
        when (event) {
            is NavigationEvent.Destination -> navController.navigate(event.route, event.builder)
            is NavigationEvent.Back -> navController.navigateUp()
            else -> Unit
        }
    }

    AnimatedNavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = Screen.Home.route,
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
        route = Screen.Home.route,
        startDestination = LeafScreen.Home.createRoute(Screen.Home)
    ) {
        addHome(Screen.Home)
        addItemDetail(Screen.Home)
        addReader(Screen.Home)
        addWebView(Screen.Home)
    }
}

private fun NavGraphBuilder.addSearchRoot() {
    navigation(
        route = Screen.Search.route,
        startDestination = LeafScreen.Search.createRoute(Screen.Search)
    ) {
        addSearch(Screen.Search)
        addItemDetail(Screen.Search)
        addReader(Screen.Search)
    }
}

private fun NavGraphBuilder.addLibraryRoot() {
    navigation(
        route = Screen.Library.route,
        startDestination = LeafScreen.Library.createRoute(Screen.Library)
    ) {
        addLibrary(Screen.Library)
        addItemDetail(Screen.Library)
        addReader(Screen.Library)
    }
}

private fun NavGraphBuilder.addPlayerLibraryRoot() {
    navigation(
        route = Screen.PlayerLibrary.route,
        startDestination = LeafScreen.PlayerLibrary.createRoute(Screen.PlayerLibrary)
    ) {
        addPlayer(Screen.PlayerLibrary)
    }
}

private fun NavGraphBuilder.addProfileRoot() {
    navigation(
        route = Screen.Profile.route,
        startDestination = LeafScreen.Profile.createRoute(Screen.Profile)
    ) {
        addProfile(Screen.Profile)
    }
}

private fun NavGraphBuilder.addHome(root: Screen) {
    composable(LeafScreen.Home.createRoute(root)) {
        Homepage()
    }
}

private fun NavGraphBuilder.addSearch(root: Screen) {
    composable(LeafScreen.Search.createRoute(root)) {
        SearchScreen()
    }
}

private fun NavGraphBuilder.addPlayer(root: Screen) {
    composable(LeafScreen.PlayerLibrary.createRoute(root)) {

    }
}

private fun NavGraphBuilder.addLibrary(root: Screen) {
    composable(LeafScreen.Library.createRoute(root)) {
        FavoriteScreen()
    }
}

private fun NavGraphBuilder.addProfile(root: Screen) {
    composable(LeafScreen.Profile.createRoute(root)) {

    }
}

private fun NavGraphBuilder.addItemDetail(root: Screen) {
    composable(
        route = LeafScreen.ContentDetail.createRoute(root),
        arguments = listOf(
            navArgument("item_id") { type = NavType.StringType }
        )
    ) {
        ItemDetail()
    }
}

private fun NavGraphBuilder.addReader(root: Screen) {
    composable(
        route = LeafScreen.Reader.createRoute(root),
        arguments = listOf(
            navArgument("fileUrl") { type = NavType.StringType }
        )
    ) {
        ReaderScreen()
    }
}

private fun NavGraphBuilder.addWebView(root: Screen) {
    composable(
        route = LeafScreen.WebView.createRoute(root),
        arguments = listOf(
            navArgument("url") { type = NavType.StringType }
        )
    ) {
        WebView(it.arguments?.getString("url").orEmpty())
    }
}
