package com.kafka.user.injection

import android.app.Activity
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.kafka.base.ActivityScope
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.data.prefs.Theme
import com.kafka.navigation.Navigator
import com.kafka.remote.config.RemoteConfig
import com.kafka.user.home.MainScreen
import com.kafka.user.home.MainViewModel
import com.kafka.user.home.bottombar.HomeNavigation
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.ui.color.ColorExtractor
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Inject
import me.tatarka.inject.annotations.Provides
import tm.alashow.datmusic.downloader.Downloader

@ActivityScope
@Component
abstract class AndroidActivityComponent(
    @get:Provides val activity: Activity,
    @Component val applicationComponent: AndroidApplicationComponent,
): SharedUiComponent {
    abstract val rootContent: RootContent
    abstract val remoteConfig: RemoteConfig

    companion object
}

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
    private val downloader: Downloader,
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
            downloader = downloader,
            home = home
        )
    }
}

interface SharedUiComponent {
    @Provides
    @ActivityScope
    fun bindRootContent(impl: DefaultRootContent): RootContent = impl
}
