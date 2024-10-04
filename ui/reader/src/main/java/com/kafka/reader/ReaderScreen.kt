package com.kafka.reader

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.reader.pdf.PdfReader
import com.kafka.reader.pdf.PdfReaderViewModel
import com.kafka.navigation.LocalNavigator
import com.kafka.navigation.Navigator
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.material.BackButton
import com.kafka.ui.components.material.TopBar

@Composable
fun ReaderScreen(viewModel: ReaderViewModel, pdfReaderViewModel: PdfReaderViewModel) {
    val viewState by viewModel.readerState.collectAsStateWithLifecycle()
    val recentItem = viewState.recentItem

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { com.kafka.reader.TopBar() }
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            if (recentItem?.localUri == null) {
                viewState.download?.downloadInfo?.let { DownloadProgress(it) }
            } else {
                PdfReader(fileId = recentItem.fileId, viewModel = pdfReaderViewModel)
            }
        }
    }
}

@Composable
private fun TopBar(navigator: Navigator = LocalNavigator.current) {
    TopBar(
        navigationIcon = { BackButton { navigator.goBack() } },
        containerColor = Color.Transparent
    )
}
