@file:Suppress("EXPERIMENTAL_API_USAGE_FUTURE_ERROR")

package com.kafka.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.plusAssign
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import kotlinx.coroutines.flow.collectLatest
import org.kafka.analytics.Logger
import org.kafka.base.debug
import org.kafka.common.logging.recomposeHighlighter
import org.kafka.navigation.NavigatorHost

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
//        PlaybackHost {
        MainScreen(navController)
    }
//    }
}

@Composable
private fun MainScreen(navController: NavHostController) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .recomposeHighlighter(),
        bottomBar = { RekhtaBottomBar(navController) }
    ) {
        debug { "Padding $it" }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = it.calculateBottomPadding())
        ) {
            val bottomSheetNavigator = rememberBottomSheetNavigator()
            navController.navigatorProvider += bottomSheetNavigator

            ModalBottomSheetLayout(bottomSheetNavigator, Modifier.fillMaxSize()) {
                AppNavigation(navController)
            }
        }
    }
}
