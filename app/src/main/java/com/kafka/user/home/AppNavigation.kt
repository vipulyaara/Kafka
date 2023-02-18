package com.kafka.user.home

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.kafka.reader.ReaderScreen
import com.kafka.search.SearchScreen
import com.kafka.user.playback.PlaybackViewModel
import com.sarahang.playback.ui.activityHiltViewModel
import com.sarahang.playback.ui.sheet.PlaybackSheet
import org.kafka.common.extensions.collectEvent
import org.kafka.homepage.Homepage
import org.kafka.item.detail.ItemDetail
import org.kafka.item.files.Files
import org.kafka.library.LibraryScreen
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LeafScreen.ItemDetail
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.NavigationEvent
import org.kafka.navigation.Navigator
import org.kafka.navigation.ROOT_SCREENS
import org.kafka.navigation.RootScreen
import org.kafka.navigation.bottomSheetScreen
import org.kafka.navigation.composableScreen
import org.kafka.webview.WebView
import org.rekhta.ui.auth.LoginScreen
import org.rekhta.ui.auth.profile.ProfileScreen

@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    navigator: Navigator = LocalNavigator.current
) {
    collectEvent(navigator.queue) { event ->
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

    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = RootScreen.Home.route,
    ) {
        addHomeRoot()
        addSearchRoot()
        addPlayerLibraryRoot()
        addLibraryRoot()
        addLogin()
    }
}

private fun NavGraphBuilder.addHomeRoot() {
    navigation(
        route = RootScreen.Home.route,
        startDestination = LeafScreen.Home().createRoute()
    ) {
        addHome(RootScreen.Home)
        addItemDetail(RootScreen.Home)
        addFiles(RootScreen.Home)
        addReader(RootScreen.Home)
        addWebView(RootScreen.Home)
        addLibrary(RootScreen.Home)
        addProfile(RootScreen.Home)
        addSearch(RootScreen.Home)
        addPlayer(RootScreen.Home)
    }
}

private fun NavGraphBuilder.addSearchRoot() {
    navigation(
        route = RootScreen.Search.route,
        startDestination = LeafScreen.Search().createRoute()
    ) {
        addSearch(RootScreen.Search)
        addItemDetail(RootScreen.Search)
        addFiles(RootScreen.Search)
        addReader(RootScreen.Search)
        addWebView(RootScreen.Search)
        addPlayer(RootScreen.Search)
    }
}

private fun NavGraphBuilder.addLibraryRoot() {
    navigation(
        route = RootScreen.Library.route,
        startDestination = LeafScreen.Library().createRoute(RootScreen.Library)
    ) {
        addLibrary(RootScreen.Library)
        addItemDetail(RootScreen.Library)
        addFiles(RootScreen.Library)
        addReader(RootScreen.Library)
        addSearch(RootScreen.Library)
        addPlayer(RootScreen.Library)
    }
}

private fun NavGraphBuilder.addPlayerLibraryRoot() {
    navigation(
        route = RootScreen.PlayerLibrary.route,
        startDestination = LeafScreen.PlayerLibrary().route
    ) {
        addPlayer(RootScreen.PlayerLibrary)
        addSearch(RootScreen.PlayerLibrary)
    }
}

private fun NavGraphBuilder.addHome(root: RootScreen) {
    composableScreen(LeafScreen.Home(rootRoute = root.route)) {
        Homepage()
    }
}

private fun NavGraphBuilder.addSearch(root: RootScreen) {
    composableScreen(LeafScreen.Search(rootRoute = root.route)) {
        SearchScreen()
    }
}

private fun NavGraphBuilder.addPlayer(root: RootScreen) {
    bottomSheetScreen(LeafScreen.PlayerLibrary(rootRoute = root.route)) {
        val navigator = LocalNavigator.current
        val playbackViewModel = activityHiltViewModel<PlaybackViewModel>()

        PlaybackSheet(
            onClose = { navigator.goBack() },
            goToItem = { playbackViewModel.goToAlbum() },
            goToCreator = { playbackViewModel.goToCreator() }
        )
    }
}

private fun NavGraphBuilder.addLibrary(root: RootScreen) {
    composableScreen(LeafScreen.Library(rootRoute = root.route)) {
        LibraryScreen()
    }
}

private fun NavGraphBuilder.addItemDetail(root: RootScreen) {
    composableScreen(ItemDetail(rootRoute = root.route)) {
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
    composableScreen(LeafScreen.WebView(rootRoute = root.route)) {
        WebView(it.arguments?.getString("url").orEmpty())
    }
}

private fun NavGraphBuilder.addLogin() {
    composableScreen(LeafScreen.Login) {
        LoginScreen()
    }
}

private fun NavGraphBuilder.addProfile(root: RootScreen) {
    bottomSheetScreen(LeafScreen.Profile(rootRoute = root.route)) {
        ProfileScreen()
    }
}

@Stable
@Composable
internal fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Search) }
    val rootScreens = ROOT_SCREENS
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            rootScreens.firstOrNull { rs -> destination.hierarchy.any { it.route == rs.route } }
                ?.let {
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
