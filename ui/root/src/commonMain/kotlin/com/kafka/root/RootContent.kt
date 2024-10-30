package com.kafka.root

import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.data.prefs.Theme
import com.kafka.navigation.Navigator
import com.kafka.root.home.MainScreen
import com.kafka.root.home.MainViewModel
import com.kafka.root.home.bottombar.HomeNavigation
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.ui.color.ColorExtractor
import me.tatarka.inject.annotations.Inject

interface RootContent {
    @Composable
    fun Content(
        navController: NavHostController,
        bottomSheetNavigator: BottomSheetNavigator,
        theme: Theme,
    )
}

@Inject
class DefaultRootContent(
    private val colorExtractor: ColorExtractor,
    private val playbackConnection: PlaybackConnection,
    private val navigator: Navigator,
    private val snackbarManager: SnackbarManager,
    private val viewModelFactory: () -> MainViewModel,
    private val home: HomeNavigation,
) : RootContent {

    @Composable
    override fun Content(
        navController: NavHostController,
        bottomSheetNavigator: BottomSheetNavigator,
        theme: Theme,
    ) {
        MainScreen(
            navController = navController,
            bottomSheetNavigator = bottomSheetNavigator,
            theme = theme,
            colorExtractor = colorExtractor,
            playbackConnection = playbackConnection,
            navigator = navigator,
            snackbarManager = snackbarManager,
            viewModelFactory = viewModelFactory,
            content = { home(navController) }
        )
    }
}
