package com.kafka.reader.pdf

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.RecentTextItem
import org.kafka.common.extensions.AnimatedVisibilityFade
import org.kafka.common.extensions.rememberMutableState
import org.kafka.common.simpleClickable
import org.kafka.ui.components.scaffoldPadding

@Composable
internal fun PdfReader(
    fileId: String,
    viewModel: PdfReaderViewModel,
    modifier: Modifier = Modifier,
) {
    val viewState by viewModel.readerState.collectAsStateWithLifecycle()

    LaunchedEffect(fileId) { viewModel.observeTextFile(fileId) }

    AnimatedVisibilityFade(viewState.recentItem != null) {
        PdfReaderWithControls(
            recentTextItem = viewState.recentItem!!,
            modifier = modifier.simpleClickable { viewModel.toggleControls() },
            onPageChanged = { viewModel.onPageChanged(fileId, it) },
            setError = { viewModel.setMessage(it) }
        )
    }
}

@Composable
private fun PdfReaderWithControls(
    recentTextItem: RecentTextItem,
    modifier: Modifier = Modifier,
    onPageChanged: (Int) -> Unit = {},
    setError: (Throwable) -> Unit = {},
) {
    val uri by rememberMutableState(recentTextItem) { recentTextItem.localUri.toUri() }

    val pdfState = remember {
        PdfState(
            uri = uri,
            initialPage = (recentTextItem.currentPage - 1).coerceAtLeast(0),
            onError = setError,
            onPageChange = onPageChanged
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        PdfViewer(
            pdfState = pdfState, modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding())
        )
    }
}
