package com.kafka.reader.viewer

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView

@Composable
fun PdfViewer(pdfState: PdfState, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = {
            PDFView(it, null).apply {
                fromUri(pdfState.uri)
                    .defaultPage(pdfState.initialPage)
                    .onPageChange { page, _ -> pdfState.onPageChange(page) }
                    .onError { t -> pdfState.onError(t) }
                    .load()
            }
        }
    ) { }
}

data class PdfState(
    val uri: Uri,
    val initialPage: Int = 0,
    val onError: (Throwable) -> Unit = {},
    val onPageChange: (Int) -> Unit = { _ -> }
)
