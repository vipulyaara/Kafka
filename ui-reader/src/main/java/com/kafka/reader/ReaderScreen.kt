package com.kafka.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import org.kafka.common.extensions.rememberStateWithLifecycle

@Composable
fun ReaderScreen(viewModel: ReaderViewModel = hiltViewModel()) {
    val viewState by rememberStateWithLifecycle(viewModel.state)
//    viewState.readerUrl?.let { ReaderView(pdfUrl = it) }

}
