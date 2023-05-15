package com.kafka.reader

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.RecentTextItem.Type
import com.kafka.reader.text.TextReader
import com.kafka.reader.viewer.PdfReader
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Navigator
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.material.BackButton
import org.kafka.ui.components.material.TopBar

@Composable
fun ReaderScreen(viewModel: ReaderViewModel = hiltViewModel()) {
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
                when (recentItem.type) {
                    Type.PDF -> PdfReader(fileId = recentItem.fileId)
                    Type.TXT -> TextReader(fileId = recentItem.fileId)
                }
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
