@file:Suppress("EXPERIMENTAL_API_USAGE_FUTURE_ERROR")

package com.kafka.user

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.sarahang.playback.ui.audio.AudioActionHost
import com.sarahang.playback.ui.audio.PlaybackHost
import kotlinx.coroutines.flow.collectLatest
import org.kafka.analytics.Logger
import org.kafka.navigation.NavigatorHost
import org.kafka.navigation.rememberBottomSheetNavigator

@Composable
fun MainScreen(analytics: Logger) {
    val navController = rememberAnimatedNavController()

    LaunchedEffect(navController, analytics) {
        navController.currentBackStackEntryFlow.collectLatest { entry ->
            analytics.logScreenView(
                label = entry.destination.displayName,
                route = entry.destination.route,
                arguments = entry.arguments,
            )
        }
    }

    NavigatorHost {
        PlaybackHost {
            AudioActionHost {
                val bottomSheetNavigator = rememberBottomSheetNavigator()
                navController.navigatorProvider += bottomSheetNavigator
                ModalBottomSheetLayout(bottomSheetNavigator, Modifier.fillMaxSize()) {
                    Home(navController)
                }
            }
        }
    }
}
