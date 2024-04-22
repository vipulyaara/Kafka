@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.kafka.user.home

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.material.bottomSheet
import com.kafka.reader.ReaderScreen
import com.kafka.reader.online.OnlineReader
import com.kafka.search.SearchScreen
import com.kafka.user.playback.PlaybackViewModel
import com.sarahang.playback.ui.sheet.PlaybackSheet
import org.kafka.base.debug
import org.kafka.common.extensions.collectEvent
import org.kafka.homepage.Homepage
import org.kafka.homepage.recent.RecentScreen
import org.kafka.item.detail.ItemDetail
import org.kafka.item.detail.description.DescriptionDialog
import org.kafka.item.files.Files
import org.kafka.library.LibraryScreen
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.NavigationEvent
import org.kafka.navigation.Navigator
import org.kafka.navigation.ROOT_SCREENS
import org.kafka.navigation.RootScreen
import org.kafka.navigation.Screen
import org.kafka.navigation.deeplink.Config
import org.kafka.webview.WebView
import org.rekhta.ui.auth.LoginScreen
import org.rekhta.ui.auth.feedback.FeedbackScreen
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
                navController.navigate(event.route)
            }

            is NavigationEvent.Back -> {
                debug { "Back pressed" }
                navController.navigateUp()
            }

            else -> Unit
        }
    }

    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        NavHost(
            modifier = modifier.fillMaxSize(),
            navController = navController,
            startDestination = RootScreen.Home.route,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(200)) },
            popExitTransition = { fadeOut(tween(200)) }
        ) {
            navigation(
                route = RootScreen.Home.route,
                startDestination = Screen.Home.createRoute(RootScreen.Home)
            ) {
                composable(Screen.Home.createRoute(RootScreen.Home)) {
                    Homepage(this)
                }

                addItemDetail(RootScreen.Home, this@SharedTransitionLayout)
                addItemDescription(RootScreen.Home)
                addFiles(RootScreen.Home)
                addReader(RootScreen.Home)
                addLibrary(RootScreen.Home)
                addProfile(RootScreen.Home)
                addFeedback(RootScreen.Home)
                addSearch(RootScreen.Home)
                addLogin(RootScreen.Home)
                addPlayer(RootScreen.Home)
                addWebView(RootScreen.Home)
                addOnlineReader(RootScreen.Home)
                addRecentItems(RootScreen.Home)
            }

            addSearchRoot(this@SharedTransitionLayout)
            addLibraryRoot(this@SharedTransitionLayout)
        }
    }
}

private fun NavGraphBuilder.addSearchRoot(sharedTransitionScope: SharedTransitionScope) {
    navigation(
        route = RootScreen.Search.route,
        startDestination = Screen.Search.createRoute(RootScreen.Search)
    ) {
        addSearch(RootScreen.Search)
        addItemDetail(RootScreen.Search, sharedTransitionScope)
        addItemDescription(RootScreen.Search)
        addFiles(RootScreen.Search)
        addReader(RootScreen.Search)
        addPlayer(RootScreen.Search)
        addWebView(RootScreen.Search)
        addOnlineReader(RootScreen.Search)
    }
}

private fun NavGraphBuilder.addLibraryRoot(sharedTransitionScope: SharedTransitionScope) {
    navigation(
        route = RootScreen.Library.route,
        startDestination = Screen.Library.createRoute(RootScreen.Library)
    ) {
        addLibrary(RootScreen.Library)
        addItemDetail(RootScreen.Library, sharedTransitionScope)
        addItemDescription(RootScreen.Library)
        addFiles(RootScreen.Library)
        addReader(RootScreen.Library)
        addSearch(RootScreen.Library)
        addPlayer(RootScreen.Library)
        addWebView(RootScreen.Library)
        addOnlineReader(RootScreen.Library)
        addLogin(RootScreen.Library)
    }
}

private fun NavGraphBuilder.addSearch(root: RootScreen) {
    composable(Screen.Search.createRoute(root)) {
        SearchScreen()
    }
}

private fun NavGraphBuilder.addPlayer(root: RootScreen) {
    bottomSheet(Screen.Player.createRoute(root)) {
        val navigator = LocalNavigator.current
        val playbackViewModel = activityHiltViewModel<PlaybackViewModel>()

        PlaybackSheet(
            onClose = { navigator.goBack() },
            goToItem = { playbackViewModel.goToAlbum() },
            goToCreator = { playbackViewModel.goToCreator() },
            playerTheme = playbackViewModel.playerTheme,
        )
    }
}

private fun NavGraphBuilder.addLibrary(root: RootScreen) {
    composable(Screen.Library.createRoute(root)) {
        LibraryScreen()
    }
}

private fun NavGraphBuilder.addItemDetail(
    root: RootScreen,
    sharedTransitionScope: SharedTransitionScope
) {
    composable(
        Screen.ItemDetail.createRoute(root),
        arguments = listOf(navArgument("itemId") { type = NavType.StringType }),
        deepLinks = listOf(
            navDeepLink { uriPattern = "${Config.BASE_URL}item/{itemId}" },
            navDeepLink { uriPattern = "${Config.BASE_URL_ALT}item/{itemId}" },
        )
    ) {
        sharedTransitionScope.ItemDetail(this)
    }
}

private fun NavGraphBuilder.addItemDescription(root: RootScreen) {
    bottomSheet(Screen.ItemDescription.createRoute(root)) {
        DescriptionDialog()
    }
}

private fun NavGraphBuilder.addFiles(root: RootScreen) {
    composable(Screen.Files.createRoute(root)) {
        Files()
    }
}

private fun NavGraphBuilder.addReader(root: RootScreen) {
    composable(Screen.Reader.createRoute(root)) {
        ReaderScreen()
    }
}

private fun NavGraphBuilder.addLogin(root: RootScreen) {
    composable(Screen.Login.createRoute(root)) {
        LoginScreen()
    }
}

private fun NavGraphBuilder.addProfile(root: RootScreen) {
    dialog(
        route = Screen.Profile.createRoute(root),
        dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        ProfileScreen()
    }
}

private fun NavGraphBuilder.addFeedback(root: RootScreen) {
    bottomSheet(route = Screen.Feedback.createRoute(root)) {
        FeedbackScreen()
    }
}

private fun NavGraphBuilder.addRecentItems(root: RootScreen) {
    composable(route = Screen.RecentItems.createRoute(root)) {
        RecentScreen()
    }
}

private fun NavGraphBuilder.addWebView(root: RootScreen) {
    composable(
        route = Screen.Web.createRoute(root),
        arguments = listOf(
            navArgument("url") { type = NavType.StringType }
        )
    ) {
        val navigator = LocalNavigator.current
        WebView(it.arguments?.getString("url").orEmpty(), navigator::goBack)
    }
}

private fun NavGraphBuilder.addOnlineReader(root: RootScreen) {
    composable(
        route = Screen.OnlineReader.createRoute(root),
        arguments = listOf(navArgument("itemId") { type = NavType.StringType })
    ) {
        OnlineReader()
    }
}

// todo: app closes on back from player

@Stable
@Composable
internal fun NavController.currentScreenAsState(): State<RootScreen> {
    val selectedItem = remember { mutableStateOf<RootScreen>(RootScreen.Home) }
    val rootScreens = ROOT_SCREENS
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            rootScreens.firstOrNull { rs -> destination.hierarchy.any { it.route == rs.route } }
                ?.let { selectedItem.value = it }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

@Composable
inline fun <reified T : ViewModel> activityHiltViewModel() =
    hiltViewModel<T>(LocalView.current.findViewTreeViewModelStoreOwner()!!)
