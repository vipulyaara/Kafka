@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.kafka.root.home.navigation

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.navigation.bottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.kafka.auth.AuthViewModel
import com.kafka.auth.LoginScreen
import com.kafka.base.debug
import com.kafka.common.animation.LocalSharedTransitionScope
import com.kafka.common.animation.ProvideLocalAnimatedContentScope
import com.kafka.common.extensions.getContext
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.homepage.Homepage
import com.kafka.homepage.HomepageViewModel
import com.kafka.homepage.recent.RecentItemsScreen
import com.kafka.homepage.recent.RecentItemsViewModel
import com.kafka.library.LibraryScreen
import com.kafka.library.bookshelf.BookshelfDetailViewModel
import com.kafka.library.bookshelf.LibraryViewModel
import com.kafka.library.bookshelf.add.AddToBookshelf
import com.kafka.library.bookshelf.add.AddToBookshelfViewModel
import com.kafka.navigation.LocalNavigator
import com.kafka.navigation.NavigationEvent
import com.kafka.navigation.Navigator
import com.kafka.navigation.deeplink.Config
import com.kafka.navigation.graph.RootScreen
import com.kafka.navigation.graph.Screen
import com.kafka.profile.ProfileScreen
import com.kafka.profile.ProfileViewModel
import com.kafka.profile.feedback.FeedbackScreen
import com.kafka.profile.feedback.FeedbackViewModel
import com.kafka.reader.epub.ReaderScreen
import com.kafka.reader.epub.ReaderViewModel
import com.kafka.root.playback.PlaybackViewModel
import com.kafka.search.SearchScreen
import com.kafka.search.SearchViewModel
import com.kafka.webview.WebView
import com.sarahang.playback.ui.playback.speed.PlaybackSpeedViewModel
import com.sarahang.playback.ui.playback.timer.SleepTimerViewModel
import com.sarahang.playback.ui.sheet.PlaybackSheet
import com.sarahang.playback.ui.sheet.ResizablePlaybackSheetLayoutViewModel
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.isDark
import ui.common.theme.theme.setStatusBarColor
import kotlin.reflect.KType

typealias AppNavigation = @Composable (NavHostController) -> Unit

@Composable
@Inject
fun AppNavigation(
    @Assisted navController: NavHostController,
    modifier: Modifier = Modifier,
    navigator: Navigator = LocalNavigator.current,
    addHome: addHome,
    addSearch: addSearch,
    addItemDetailGroup: addItemDetailGroup,
    addLibrary: addLibrary,
    addProfile: addProfile,
    addProfileScreen: addProfileScreen,
    addFeedback: addFeedback,
    addLogin: addLogin,
    addPlayer: addPlayer,
    addWebView: addWebView,
    addRecentItems: addRecentItems
) {
    CollectEvent(navigator.queue) { event ->
        when (event) {
            is NavigationEvent.Destination -> {
                when (val screen = event.route) {
                    is Screen.ItemDescription -> {
                        navController.navigate(Screen.ItemDescription.route(screen.itemId))
                    }

                    is Screen.AddToBookshelf -> {
                        navController.navigate(Screen.AddToBookshelf.route(screen.itemId))
                    }

                    Screen.Feedback -> {
                        navController.navigate(Screen.Feedback.route)
                    }

                    is Screen.ReportContent -> {
                        navController.navigate(Screen.ReportContent.route(screen.itemId))
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

    SwitchStatusBarsOnPlayer(navController = navController)

    SharedTransitionLayout {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
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
                    addItemDetailGroup()
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
                    addItemDetailGroup()
                    addPlayer()
                    addWebView()
                }

                navigation<RootScreen.Library>(startDestination = Screen.Library) {
                    addLibrary()
                    addItemDetailGroup()
                    addSearch()
                    addPlayer()
                    addWebView()
                    addLogin()
                    addProfile()
                }

                navigation<RootScreen.Profile>(startDestination = Screen.Profile) {
                    addProfileScreen()
                    addPlayer()
                    addWebView()
                    addLogin()
                    addFeedback()
                }
            }
        }
    }
}

typealias addHome = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addHome(viewModelFactory: () -> HomepageViewModel) {
    composableScreen<Screen.Home> {
        Homepage(viewModelFactory = viewModelFactory)
    }
}

typealias addSearch = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addSearch(viewModelFactory: (SavedStateHandle) -> SearchViewModel) {
    composable<Screen.Search>(
        deepLinks = listOf(
            navDeepLink<Screen.Search>(basePath = "${Config.BASE_URL}search"),
            navDeepLink<Screen.Search>(basePath = "${Config.BASE_URL_ALT}search")
        )
    ) {
        SearchScreen(viewModelFactory)
    }
}

typealias addPlayer = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addPlayer(
    snackbarManager: SnackbarManager,
    viewModelFactory: () -> PlaybackViewModel,
    resizableViewModelFactory: () -> ResizablePlaybackSheetLayoutViewModel,
    sleepTimerViewModelFactory: () -> SleepTimerViewModel,
    playbackSpeedViewModelFactory: () -> PlaybackSpeedViewModel
) {
    bottomSheet(Screen.Player.route) {
        val navigator = LocalNavigator.current
        val viewModel = viewModel { viewModelFactory() }

        PlaybackSheet(
            onClose = { navigator.goBack() },
            goToItem = { viewModel.goToAlbum() },
            goToCreator = { viewModel.goToCreator() },
            playerTheme = viewModel.playerTheme,
            useDarkTheme = LocalTheme.current.isDark(),
            resizableViewModelFactory = resizableViewModelFactory,
            sleepTimerViewModelFactory = sleepTimerViewModelFactory,
            playbackSpeedViewModelFactory = playbackSpeedViewModelFactory,
            showMessage = { snackbarManager.addMessage(UiMessage(it)) }
        )
    }
}

typealias addLibrary = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addLibrary(
    bookshelfFactory: () -> LibraryViewModel,
    detailFactory: (String) -> BookshelfDetailViewModel
) {
    composableScreen<Screen.Library> {
        LibraryScreen(bookshelfFactory = bookshelfFactory, detailFactory = detailFactory)
    }
}

typealias addLogin = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addLogin(viewModelFactory: () -> AuthViewModel) {
    composable<Screen.Login> {
        val viewModel = viewModel { viewModelFactory() }
        LoginScreen(viewModel)
    }
}

typealias addProfile = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addProfile(viewModelFactory: () -> ProfileViewModel) {
    dialog<Screen.Profile>(dialogProperties = DialogProperties(usePlatformDefaultWidth = false)) {
        val viewModel = viewModel { viewModelFactory() }
        ProfileScreen(viewModel)
    }
}

typealias addProfileScreen = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addProfileScreen(viewModelFactory: () -> ProfileViewModel) {
    composable<Screen.Profile> {
        val viewModel = viewModel { viewModelFactory() }
        ProfileScreen(profileViewModel = viewModel, modifier = Modifier.systemBarsPadding())
    }
}

typealias addFeedback = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addFeedback(viewModelFactory: () -> FeedbackViewModel) {
    bottomSheet(route = Screen.Feedback.route) {
        val viewModel = viewModel { viewModelFactory() }
        FeedbackScreen(viewModel)
    }
}

typealias addRecentItems = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addRecentItems(viewModelFactory: () -> RecentItemsViewModel) {
    composable<Screen.RecentItems> {
        val viewModel = viewModel { viewModelFactory() }
        RecentItemsScreen(viewModel)
    }
}

typealias addWebView = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addWebView() {
    composable<Screen.Web> { backStackEntry ->
        val navigator = LocalNavigator.current
        WebView(backStackEntry.toRoute<Screen.Web>().url, navigator::goBack)
    }
}

typealias addEpubReader = NavGraphBuilder.() -> Unit

@Inject
fun NavGraphBuilder.addEpubReader(
    viewModelFactory: (SavedStateHandle) -> ReaderViewModel,
) {
    composable<Screen.EpubReader> {
        val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
        ReaderScreen(viewModel = viewModel)
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
internal fun SwitchStatusBarsOnPlayer(navController: NavHostController) {
    val currentRoute by navController.currentBackStackEntryAsState()
    val destination = currentRoute?.destination?.route?.substringBefore("/")
    val isPlayerUp = destination == "player"

    val context = getContext()
    val useDarkTheme = LocalTheme.current.isDark()

    DisposableEffect(isPlayerUp, useDarkTheme) {
        setStatusBarColor(context, if (isPlayerUp) useDarkTheme else !useDarkTheme)

        onDispose {

        }
    }
}

@Composable
fun <T> CollectEvent(
    flow: Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: (T) -> Unit,
): Unit = LaunchedEffect(lifecycle, flow) {
    lifecycle.repeatOnLifecycle(minActiveState) {
        flow.collect {
            collector(it)
        }
    }
}

inline fun <reified T : Screen> NavGraphBuilder.composableScreen(
    typeMap: Map<KType, NavType<*>> = emptyMap(),
    deepLinks: List<NavDeepLink> = emptyList(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(typeMap = typeMap, deepLinks = deepLinks) {
        ProvideLocalAnimatedContentScope(this@composable) {
            content(it)
        }
    }
}
