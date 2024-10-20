package com.kafka.reader.pdf

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kafka.common.extensions.AnimatedVisibilityFade
import com.kafka.common.extensions.rememberMutableState
import com.kafka.common.simpleClickable
import com.kafka.data.entities.RecentTextItem
import com.kafka.ui.components.scaffoldPadding

@Composable
fun PdfReader(
    viewModelFactory: (SavedStateHandle) -> PdfReaderViewModel,
    modifier: Modifier = Modifier,
) {
    val viewModel = viewModel { viewModelFactory(createSavedStateHandle()) }
    val viewState by viewModel.readerState.collectAsStateWithLifecycle()

    AnimatedVisibilityFade(viewState.recentItem != null) {
        PdfReaderWithControls(
            recentTextItem = viewState.recentItem!!,
            modifier = modifier.simpleClickable { viewModel.toggleControls() },
            onPageChanged = { viewModel.onPageChanged(it) },
            setError = { viewModel.setMessage(it) }
        )
    }
}

@Composable
fun PdfReader(
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
    val uri by rememberMutableState(recentTextItem) { recentTextItem.localUri }

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
