package com.kafka.reader.pdf

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle

@Composable
actual fun PdfViewer(pdfState: PdfState, modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = {
            PDFView(it, null).apply {
                maxZoom = MaxZoom
                fromUri(pdfState.uri.toUri())
                    .defaultPage(pdfState.initialPage)
                    .spacing(12)
                    .onPageChange { page, _ -> pdfState.onPageChange(page) }
                    .scrollHandle(DefaultScrollHandle(it))
                    .onError { t -> pdfState.onError(t) }
                    .load()
            }
        }
    ) { }
}
