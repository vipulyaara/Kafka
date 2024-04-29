package com.kafka.reader.pdf

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle

@Composable
fun PdfViewer(pdfState: PdfState, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = {
            PDFView(it, null).apply {
                maxZoom = MaxZoom
                fromUri(pdfState.uri)
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

data class PdfState(
    val uri: Uri,
    val initialPage: Int = 0,
    val onError: (Throwable) -> Unit = {},
    val onPageChange: (Int) -> Unit = { _ -> }
)

const val MaxZoom = 10f
