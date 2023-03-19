package com.kafka.textreader

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class BouquetPdfRender(
    private val fileDescriptor: ParcelFileDescriptor,
    val width: Int,
    val height: Int,
    val portrait: Boolean
) {
    private val pdfRenderer = PdfRenderer(fileDescriptor)
    val pageCount get() = pdfRenderer.pageCount
    private val mutex: Mutex = Mutex()
    private val coroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    val pageLists: List<Page> = List(pdfRenderer.pageCount) {
        Page(
            mutex = mutex,
            index = it,
            pdfRenderer = pdfRenderer,
            coroutineScope = coroutineScope,
            width = width,
            height = height,
            portrait = portrait
        )
    }

    fun close() {
        coroutineScope.launch {
            pageLists.forEach { it.job?.cancelAndJoin() }
            pdfRenderer.close()
            fileDescriptor.close()
        }
    }

    class Page(
        val mutex: Mutex,
        val index: Int,
        val pdfRenderer: PdfRenderer,
        val coroutineScope: CoroutineScope,
        width: Int,
        height: Int,
        portrait: Boolean
    ) {
        val dimension = pdfRenderer.openPage(index).use {
            if (portrait) {
                val h = it.height * (width.toFloat() / it.width)
                val dim = Dimension(
                    height = h.toInt(),
                    width = width
                )
                dim
            } else {
                val w = it.width * (height.toFloat() / it.height)
                val dim = Dimension(
                    height = height,
                    width = w.toInt()
                )
                dim
            }
        }

        var job: Job? = null

        val stateFlow = MutableStateFlow(createBlankBitmap())

        var isLoaded = false

        fun load() {
            if (!isLoaded) {
                job = coroutineScope.launch {
                    mutex.withLock {
                        val newBitmap = createBlankBitmap()
                        pdfRenderer.openPage(index).use { currentPage ->
                            currentPage.render(
                                newBitmap,
                                null,
                                null,
                                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                            )
                        }
                        isLoaded = true
                        stateFlow.emit(newBitmap)
                    }
                }
            }
        }

        fun recycle() {
            isLoaded = false
            stateFlow.tryEmit(createBlankBitmap())
        }

        private fun createBlankBitmap(): Bitmap {
            return createBitmap(
                dimension.width,
                dimension.height,
                Bitmap.Config.ARGB_8888
            ).apply {
                val canvas = Canvas(this)
                canvas.drawColor(android.graphics.Color.WHITE)
                canvas.drawBitmap(this, 0f, 0f, null)
            }
        }

        data class Dimension(
            val height: Int,
            val width: Int
        )
    }
}
