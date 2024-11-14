package com.kafka.kms.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kafka.kms.components.Sidebar
import com.kafka.kms.ui.directory.FileTree
import com.kafka.kms.ui.gutenberg.GutenbergScreen
import com.kafka.kms.ui.gutenberg.GutenbergViewModel
import me.tatarka.inject.annotations.Inject

typealias KmsHomepage = @Composable () -> Unit

@Inject
@Composable
fun KmsHomepage(gutenbergFactory: () -> GutenbergViewModel) {
    var currentRoute by remember { mutableStateOf("gutenberg") }

    Row(modifier = Modifier.fillMaxSize()) {
        // Left sidebar
        Sidebar(
            selectedRoute = currentRoute,
            modifier = Modifier.weight(0.2f),
            onRouteSelected = { route -> currentRoute = route }
        )

        // Main content area with padding
        Box(modifier = Modifier.weight(1f)) {
            when (currentRoute) {
                "gutenberg" -> GutenbergScreen(
                    viewModel = gutenbergFactory(),
                    modifier = Modifier.fillMaxSize()
                )
                "books" -> { /* TODO: Implement Books screen */ }
                "standard-ebooks" -> { /* TODO: Implement Standard Ebooks screen */ }
                "settings" -> { /* TODO: Implement Settings screen */ }
            }
        }

        // Right sidebar with FileTree
        FileTree(
            rootPath = System.getProperty("user.home") + "/StudioProjects/kms-tools/ebooks",
            modifier = Modifier.weight(0.25f),
            onFileSelected = { /* Handle file selection */ }
        )
    }
}
