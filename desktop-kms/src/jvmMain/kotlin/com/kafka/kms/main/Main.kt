package com.kafka.kms.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.kafka.data.prefs.Theme
import com.kafka.data.prefs.observeTheme
import com.kafka.kms.components.theme.AppleTheme

fun main() = application {
    Window(
        title = "KMS",
        state = rememberWindowState(placement = WindowPlacement.Fullscreen),
        onCloseRequest = ::exitApplication,
    ) {
        val applicationComponent = remember {
            KmsApplicationComponent::class.create()
        }

        val component = remember(applicationComponent) {
            KmsWindowComponent::class.create(applicationComponent)
        }

        component.appInitializers.forEach { it.init() }

        val theme by applicationComponent.preferencesStore.observeTheme()
            .collectAsStateWithLifecycle(Theme.SYSTEM)
        val navController = rememberNavController()

        AppleTheme(false) {
            component.kmsHomepage()
        }
    }
}