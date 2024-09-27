package com.kafka.user.home

//noinspection UsingMaterialAndMaterial3Libraries
import android.app.Activity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.kafka.reader.ReaderScreen
import com.kafka.reader.online.OnlineReader
import com.kafka.search.SearchScreen
import com.kafka.user.playback.PlaybackViewModel
import com.sarahang.playback.ui.sheet.PlaybackSheet
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import org.kafka.base.debug
import org.kafka.common.extensions.CollectEvent
import org.kafka.homepage.Homepage
import org.kafka.homepage.recent.RecentItemsScreen
import org.kafka.item.detail.ItemDetail
import org.kafka.item.detail.description.DescriptionDialog
import org.kafka.item.files.Files
import org.kafka.library.LibraryScreen
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.NavigationEvent
import org.kafka.navigation.Navigator
import org.kafka.navigation.deeplink.Config
import org.kafka.navigation.graph.RootScreen
import org.kafka.navigation.graph.Screen
import org.kafka.summary.SummaryScreen
import org.kafka.webview.WebView
import org.rekhta.ui.auth.LoginScreen
import org.rekhta.ui.profile.ProfileScreen
import org.rekhta.ui.profile.feedback.FeedbackScreen
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.shouldUseDarkColors

typealias AppNavigation = @Composable (NavHostController) -> Unit

@Composable
@Inject
internal fun AppNavigation(
    @Assisted navController: NavHostController,
    modifier: Modifier = Modifier,
    navigator: Navigator = LocalNavigator.current,
    addHome: addHome,
    addSearch: addSearch,
    addItemDetailGroup: addItemDetailGroup,
    addLibrary: addLibrary,
    addProfile: addProfile,
    addFeedback: addFeedback,
    addLogin: addLogin,
    addPlayer: addPlayer,
    addWebView: addWebView,
    addRecentItems: addRecentItems,
) {
    CollectEvent(navigator.queue) { event ->
        when (event) {
            is NavigationEvent.Destination -> {
                when (val screen = event.route) {
                    is Screen.ItemDetail -> {
                        navController.navigate(Screen.ItemDetail.route(screen.itemId))
                    }

                    is Screen.ItemDescription -> {
                        navController.navigate(Screen.ItemDescription.route(screen.itemId))
                    }

                    Screen.Feedback -> {
                        navController.navigate(Screen.Player.route)
                    }

                    Screen.Player -> {
                        navController.navigate(Screen.Player.route)
                    }

                    else -> {
                        navController.navigate(screen)
                    }
                }
            }

            is NavigationEvent.Back -> {
                debug { "Back pressed" }
                navController.navigateUp()
            }

            else -> Unit
        }
    }

    SwitchStatusBarsOnPlayer(navController)

    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = RootScreen.Home,
        enterTransition = { enter() },
        exitTransition = { fadeOut() },
        popEnterTransition = { fadeIn() },
        popExitTransition = { exit() }
    ) {
        navigation<RootScreen.Home>(startDestination = Screen.Home) {
            addHome()
            addItemDetailGroup(navController)
            addLibrary()
            addProfile()
            addFeedback()
            addSearch()
            addLogin()
            addPlayer()
            addWebView()
            addRecentItems()
        }

        navigation<RootScreen.Search>(startDestination = Screen.Search()) {
            addSearch()
            addItemDetailGroup(navController)
            addPlayer()
            addWebView()
        }

        navigation<RootScreen.Library>(startDestination = Screen.Library) {
            addLibrary()
            addItemDetailGroup(navController)
            addSearch()
            addPlayer()
            addWebView()
            addLogin()
            addProfile()
        }
    }
}

typealias addItemDetailGroup = NavGraphBuilder.(NavController) -> Unit

@Inject
internal fun NavGraphBuilder.addItemDetailGroup(
    @Assisted navController: NavController,
    addItemDetail: addItemDetail,
    addItemDescription: addItemDescription,
    addFiles: addFiles,
    addReader: addReader,
    addOnlineReader: addOnlineReader,
    addSummary: addSummary,
) {
    addItemDetail()
    addItemDescription()
    addFiles()
    addReader()
    addOnlineReader(navController)
    addSummary()
}

typealias addHome = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addHome(homepage: Homepage) {
    composable<Screen.Home> {
        homepage()
    }
}

typealias addSearch = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addSearch() {
    composable<Screen.Search>(
        deepLinks = listOf(
            navDeepLink<Screen.Search>("${Config.BASE_URL}search"),
            navDeepLink<Screen.Search>("${Config.BASE_URL_ALT}search")
        )
    ) {
        SearchScreen()
    }
}

typealias addPlayer = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addPlayer() {
    bottomSheet(Screen.Player.route) {
        val navigator = LocalNavigator.current
        val playbackViewModel = activityHiltViewModel<PlaybackViewModel>()

        PlaybackSheet(
            onClose = { navigator.goBack() },
            goToItem = { playbackViewModel.goToAlbum() },
            goToCreator = { playbackViewModel.goToCreator() },
            playerTheme = playbackViewModel.playerTheme,
            useDarkTheme = LocalTheme.current.shouldUseDarkColors()
        )
    }
}

typealias addLibrary = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addLibrary() {
    composable<Screen.Library> {
        LibraryScreen()
    }
}

typealias addItemDetail = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addItemDetail(itemDetail: ItemDetail) {
    composable(
        route = Screen.ItemDetail.route,
        deepLinks = listOf(
            navDeepLink<Screen.ItemDetail>("${Config.BASE_URL}item"),
            navDeepLink<Screen.ItemDetail>("${Config.BASE_URL_ALT}item")
        )
    ) {
        itemDetail()
    }
}

typealias addItemDescription = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addItemDescription() {
    bottomSheet(Screen.ItemDescription.route) {
        DescriptionDialog()
    }
}

typealias addFiles = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addFiles() {
    composable<Screen.Files> {
        Files()
    }
}

typealias addReader = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addReader() {
    composable<Screen.Reader> {
        ReaderScreen()
    }
}

typealias addLogin = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addLogin() {
    composable<Screen.Login> {
        LoginScreen()
    }
}

typealias addProfile = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addProfile() {
    dialog<Screen.Profile>(dialogProperties = DialogProperties(usePlatformDefaultWidth = false)) {
        ProfileScreen()
    }
}

typealias addFeedback = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addFeedback() {
    bottomSheet(route = Screen.Feedback.route) {
        FeedbackScreen()
    }
}

typealias addRecentItems = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addRecentItems() {
    composable<Screen.RecentItems> {
        RecentItemsScreen()
    }
}

typealias addSummary = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addSummary() {
    composable<Screen.Summary> {
        SummaryScreen()
    }
}

typealias addWebView = NavGraphBuilder.() -> Unit

@Inject
internal fun NavGraphBuilder.addWebView() {
    composable<Screen.Web> { backStackEntry ->
        val navigator = LocalNavigator.current
        WebView(backStackEntry.toRoute<Screen.Web>().url, navigator::goBack)
    }
}

typealias addOnlineReader = NavGraphBuilder.(NavController) -> Unit

@Inject
internal fun NavGraphBuilder.addOnlineReader(@Assisted navController: NavController) {
    composable<Screen.OnlineReader> {
        val currentDestination = navController.currentDestination?.route

        OnlineReader { fileId ->
            navController.navigate(Screen.Reader(fileId)) {
                popUpTo(currentDestination.orEmpty()) { inclusive = true }
            }
        }
    }
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.enter(): EnterTransition {
    val initialNavGraph = initialState.destination.parent?.route
    val targetNavGraph = targetState.destination.parent?.route

    if (initialNavGraph != targetNavGraph) {
        return fadeIn()
    }

    return slideIntoContainer(Start) { (it / 1.5).toInt() } + fadeIn()
}

fun AnimatedContentTransitionScope<NavBackStackEntry>.exit(): ExitTransition {
    val initialNavGraph = initialState.destination.parent?.route
    val targetNavGraph = targetState.destination.parent?.route

    if (initialNavGraph != targetNavGraph) {
        return fadeOut()
    }

    return slideOutOfContainer(End) { (it / 1.5).toInt() } + fadeOut()
}

@Composable
inline fun <reified T : ViewModel> activityHiltViewModel() =
    hiltViewModel<T>(LocalView.current.findViewTreeViewModelStoreOwner()!!)

@Composable
internal fun SwitchStatusBarsOnPlayer(navController: NavHostController) {
    val currentRoute by navController.currentBackStackEntryAsState()
    val destination = currentRoute?.destination?.route?.substringBefore("/")
    val isPlayerUp = destination == "player"

    val view = LocalView.current
    val useDarkTheme = LocalTheme.current.shouldUseDarkColors()

    DisposableEffect(isPlayerUp, useDarkTheme) {
        val activity = view.context as Activity
        WindowCompat.getInsetsController(activity.window, activity.window.decorView).apply {
            isAppearanceLightStatusBars = if (isPlayerUp) useDarkTheme else !useDarkTheme
        }

        onDispose {

        }
    }
}
