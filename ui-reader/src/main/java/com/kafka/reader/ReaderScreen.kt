package com.kafka.reader

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.RecentTextItem
import com.kafka.reader.pdf.PdfReader
import com.kafka.reader.text.TextReader
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Navigator
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.material.BackButton
import org.kafka.ui.components.material.TopBar

@Composable
fun ReaderScreen(viewModel: ReaderViewModel = hiltViewModel()) {
    val viewState by viewModel.readerState.collectAsStateWithLifecycle()
    val file = viewState.recentTextItem

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { com.kafka.reader.TopBar() }
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            when (file?.type) {
                RecentTextItem.Type.PDF -> PdfReader(fileId = file.id)
                RecentTextItem.Type.TXT -> TextReader(fileId = file.id)
                else -> viewState.download?.downloadInfo?.let { DownloadProgress(it) }
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
