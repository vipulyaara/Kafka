package com.kafka.reader.pdf

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.TextFile
import com.kafka.reader.controls.GoToPage
import com.kafka.textreader.bouquet.ResourceType
import com.kafka.textreader.bouquet.VerticalPdfReader
import com.kafka.textreader.bouquet.rememberVerticalPdfReaderState
import org.kafka.common.animation.Delayed
import org.kafka.common.extensions.AnimatedVisibility
import org.kafka.common.extensions.rememberMutableState
import org.kafka.common.simpleClickable
import org.kafka.ui.components.scaffoldPadding

@Composable
internal fun PdfReader(
    fileId: String,
    modifier: Modifier = Modifier,
    viewModel: PdfReaderViewModel = hiltViewModel(),
) {
    val viewState by viewModel.readerState.collectAsStateWithLifecycle()

    val currentPage = viewModel.currentPage
    val showControls = viewModel.showControls

    LaunchedEffect(fileId) {
        viewModel.observeTextFile(fileId)
    }

    LaunchedEffect(fileId, currentPage) {
        viewModel.onPageChanged(fileId, currentPage)
    }

    AnimatedVisibility(viewState.textFile != null) {
        Delayed {
            PdfReaderWithControls(
                textFile = viewState.textFile!!,
                currentPage = currentPage,
                modifier = modifier.simpleClickable { viewModel.toggleControls() },
                listState = viewModel.lazyListState,
                goToPage = viewModel::goToPage,
                showControls = showControls
            )
        }
    }
}

@Composable
private fun PdfReaderWithControls(
    textFile: TextFile,
    currentPage: Int,
    goToPage: (Int) -> Unit,
    modifier: Modifier = Modifier,
    showControls: Boolean = false,
    listState: LazyListState = rememberLazyListState()
) {
    val scaffoldPadding = scaffoldPadding()
    val uri by rememberMutableState(textFile) { textFile.localUri.toUri() }

    Box(modifier = modifier.fillMaxSize()) {
        val pdfState = rememberVerticalPdfReaderState(
            resource = ResourceType.Local(uri),
            lazyListState = listState
        )
        VerticalPdfReader(state = pdfState, contentPadding = scaffoldPadding)

        GoToPage(
            showControls = showControls,
            currentPage = currentPage,
            goToPage = goToPage,
            scaffoldPadding = scaffoldPadding
        )
    }
}
