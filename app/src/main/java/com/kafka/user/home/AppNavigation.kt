package com.kafka.user.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import com.google.accompanist.navigation.material.bottomSheet
import com.kafka.reader.ReaderScreen
import com.kafka.search.SearchScreen
import com.kafka.user.playback.PlaybackViewModel
import com.sarahang.playback.ui.activityHiltViewModel
import com.sarahang.playback.ui.sheet.PlaybackSheet
import org.kafka.base.debug
import org.kafka.common.extensions.collectEvent
import org.kafka.homepage.Homepage
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
import org.rekhta.ui.auth.LoginScreen
import org.rekhta.ui.auth.feedback.FeedbackScreen
import org.rekhta.ui.auth.profile.ProfileScreen
import ui.common.theme.theme.Dimens

@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    navigator: Navigator = LocalNavigator.current
) {
    collectEvent(navigator.queue) { event ->
        when (event) {
            is NavigationEvent.Destination -> navController.navigate(event.route)
            is NavigationEvent.Back -> {
                debug { "Back pressed" }
                navController.navigateUp()
            }
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
        addLibraryRoot()
    }
}

private fun NavGraphBuilder.addHomeRoot() {
    navigation(
        route = RootScreen.Home.route,
        startDestination = Screen.Home.createRoute(RootScreen.Home)
    ) {
        addHome(RootScreen.Home)
        addItemDetail(RootScreen.Home)
        addItemDescription(RootScreen.Home)
        addFiles(RootScreen.Home)
        addReader(RootScreen.Home)
        addLibrary(RootScreen.Home)
        addProfile(RootScreen.Home)
        addFeedback(RootScreen.Home)
        addSearch(RootScreen.Home)
        addPlayer(RootScreen.Home)
        addLogin(RootScreen.Home)
    }
}

private fun NavGraphBuilder.addSearchRoot() {
    navigation(
        route = RootScreen.Search.route,
        startDestination = Screen.Search.createRoute(RootScreen.Search)
    ) {
        addSearch(RootScreen.Search)
        addItemDetail(RootScreen.Search)
        addItemDescription(RootScreen.Search)
        addFiles(RootScreen.Search)
        addReader(RootScreen.Search)
        addPlayer(RootScreen.Search)
    }
}

private fun NavGraphBuilder.addLibraryRoot() {
    navigation(
        route = RootScreen.Library.route,
        startDestination = Screen.Library.createRoute(RootScreen.Library)
    ) {
        addLibrary(RootScreen.Library)
        addItemDetail(RootScreen.Library)
        addItemDescription(RootScreen.Library)
        addFiles(RootScreen.Library)
        addReader(RootScreen.Library)
        addSearch(RootScreen.Library)
        addPlayer(RootScreen.Library)
    }
}

private fun NavGraphBuilder.addHome(root: RootScreen) {
    composable(Screen.Home.createRoute(root)) {
        Homepage()
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
            goToCreator = { playbackViewModel.goToCreator() }
        )
    }
}

private fun NavGraphBuilder.addLibrary(root: RootScreen) {
    composable(Screen.Library.createRoute(root)) {
        LibraryScreen()
    }
}

private fun NavGraphBuilder.addItemDetail(root: RootScreen) {
    composable(Screen.ItemDetail.createRoute(root)) {
        ItemDetail()
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
    bottomSheet(
        route = Screen.Profile.createRoute(root)
    ) {
        ProfileScreen()
    }
}

private fun NavGraphBuilder.addFeedback(root: RootScreen) {
    bottomSheet(
        route = Screen.Feedback.createRoute(root)
    ) {
        FeedbackScreen()
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
