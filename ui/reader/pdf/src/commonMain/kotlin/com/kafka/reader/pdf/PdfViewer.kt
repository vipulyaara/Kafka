package com.kafka.reader.pdf

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PdfViewer(pdfState: PdfState, modifier: Modifier = Modifier)

data class PdfState(
    val uri: String,
    val initialPage: Int = 0,
    val onError: (Throwable) -> Unit = {},
    val onPageChange: (Int) -> Unit = { _ -> }
)

const val MaxZoom = 10f
