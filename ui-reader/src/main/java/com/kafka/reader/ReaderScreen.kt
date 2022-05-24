package com.kafka.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import org.kafka.base.debug
import org.kafka.common.extensions.rememberStateWithLifecycle

@Composable
fun ReaderScreen(viewModel: ReaderViewModel = hiltViewModel()) {
    val viewState by rememberStateWithLifecycle(viewModel.state)

    viewState.readerUrl?.let {
        debug { "Opening reader with url $it" }
        ReaderView(pdfUrl = it)
    }
}
