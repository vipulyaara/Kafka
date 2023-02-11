package com.kafka.reader.pdf

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.TextFile
import com.kafka.reader.controls.GoToPage
import com.kafka.textreader.bouquet.ResourceType
import com.kafka.textreader.bouquet.VerticalPdfReader
import com.kafka.textreader.bouquet.ZoomableImage
import com.kafka.textreader.bouquet.rememberVerticalPdfReaderState
import kotlinx.coroutines.launch
import org.kafka.common.extensions.AnimatedVisibility
import org.kafka.common.extensions.rememberMutableState
import org.kafka.common.simpleClickable
import org.kafka.ui.components.progress.InfiniteProgressBar
import org.kafka.ui.components.scaffoldPadding

@Composable
internal fun PdfReader(
    fileId: String,
    modifier: Modifier = Modifier,
    viewModel: PdfReaderViewModel = hiltViewModel(),
) {
    val viewState by viewModel.readerState.collectAsStateWithLifecycle()
    val showControls = viewModel.showControls

    LaunchedEffect(fileId) { viewModel.observeTextFile(fileId) }

    AnimatedVisibility(viewState.textFile != null) {
//        Delayed {
            PdfReaderWithControls(
                textFile = viewState.textFile!!,
                modifier = modifier.simpleClickable { viewModel.toggleControls() },
                setControls = viewModel::showControls,
                showControls = showControls,
                onPageChanged = { viewModel.onPageChanged(fileId, it) }
            )
//        }
    }
}

@Composable
private fun PdfReaderWithControls(
    textFile: TextFile,
    modifier: Modifier = Modifier,
    showControls: Boolean = false,
    setControls: (Boolean) -> Unit = {},
    onPageChanged: (Int) -> Unit = {},
) {
    val scaffoldPadding = scaffoldPadding()
    val scope = rememberCoroutineScope()
    val uri by rememberMutableState(textFile) { textFile.localUri.toUri() }
    val pdfState = rememberVerticalPdfReaderState(
        resource = ResourceType.Local(uri),
        startPage = (textFile.currentPage - 1).coerceAtLeast(0)
    )
    val currentPage by remember { derivedStateOf { pdfState.currentPage } }
    var panEnabled by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(textFile, currentPage) {
        onPageChanged(currentPage)
        panEnabled = false
    }

    Box(modifier = modifier.fillMaxSize()) {
        InfiniteProgressBar(modifier = Modifier.align(Alignment.Center))
        ZoomableImage(panEnabled = panEnabled) {
            VerticalPdfReader(state = pdfState, contentPadding = scaffoldPadding)
        }

        GoToPage(
            showControls = showControls,
            currentPage = currentPage,
            goToPage = {
                scope.launch { pdfState.lazyState.animateScrollToItem(it) }
                setControls(false)
            },
            scaffoldPadding = scaffoldPadding
        )
    }
}
