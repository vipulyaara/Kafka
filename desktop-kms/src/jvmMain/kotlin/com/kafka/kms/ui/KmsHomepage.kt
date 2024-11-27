package com.kafka.kms.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kafka.data.prefs.PreferencesStore
import com.kafka.data.prefs.THEME
import com.kafka.data.prefs.Theme
import com.kafka.data.prefs.observeTheme
import com.kafka.kms.components.Sidebar
import com.kafka.kms.data.files.DirectoryPaths
import com.kafka.kms.data.remote.SupabaseUploadService
import com.kafka.kms.ui.books.BooksScreen
import com.kafka.kms.ui.books.BooksViewModel
import com.kafka.kms.ui.directory.FileTree
import com.kafka.kms.ui.gutenberg.GutenbergScreen
import com.kafka.kms.ui.gutenberg.GutenbergViewModel
import com.kafka.kms.ui.librivox.LibrivoxScreen
import com.kafka.kms.ui.librivox.LibrivoxViewModel
import com.kafka.kms.ui.upload.UploadScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

typealias KmsHomepage = @Composable () -> Unit

@Inject
@Composable
fun KmsHomepage(
    uploadService: SupabaseUploadService,
    gutenbergFactory: () -> GutenbergViewModel,
    librivoxFactory: () -> LibrivoxViewModel,
    booksFactory: () -> BooksViewModel,
    preferencesStore: PreferencesStore,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    var currentRoute by remember { mutableStateOf("upload") }
    var theme by remember { mutableStateOf(Theme.SYSTEM) }
    
    LaunchedEffect(Unit) {
        preferencesStore.observeTheme()
            .collect { theme = it }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // Left sidebar with integrated theme switch
        Sidebar(
            selectedRoute = currentRoute,
            modifier = Modifier.weight(0.2f),
            onRouteSelected = { route -> currentRoute = route },
            theme = theme,
            onThemeChange = { newTheme ->
                scope.launch {
                    preferencesStore.save(THEME, newTheme.name)
                }
            }
        )

        // Main content area with padding
        Box(modifier = Modifier.weight(1f)) {
            when (currentRoute) {
                "gutenberg" -> GutenbergScreen(
                    viewModel = viewModel { gutenbergFactory()  } ,
                    modifier = Modifier.fillMaxSize()
                )

                "librivox" -> LibrivoxScreen(
                    viewModel = viewModel { librivoxFactory()},
                    modifier = Modifier.fillMaxSize()
                )

                "upload" -> UploadScreen(
                    uploadService = uploadService,
                    modifier = Modifier.fillMaxSize()
                )

                "books" -> { BooksScreen(viewModel { booksFactory()}) }

                "standard-ebooks" -> { /* TODO: Implement Standard Ebooks screen */
                }

                "settings" -> { /* TODO: Implement Settings screen */
                }
            }
        }

        // Right sidebar with FileTree
        FileTree(
            rootPath = System.getProperty("user.home") + "/StudioProjects/kms-tools",
            modifier = Modifier.weight(0.25f),
            onFileSelected = { /* Handle file selection */ },
            defaultExpandedDirs = listOf(
                DirectoryPaths.textPath("kafka_franz-metamorphosis").absolutePath
            )
        )
    }
}
