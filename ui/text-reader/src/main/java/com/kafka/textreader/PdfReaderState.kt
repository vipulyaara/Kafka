package com.kafka.textreader

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import java.io.File

abstract class PdfReaderState(
    val resource: ResourceType,
    isZoomEnable: Boolean = false
) {
    internal var mError by mutableStateOf<Throwable?>(null)
    val error: Throwable?
        get() = mError

    internal var mIsZoomEnable by mutableStateOf(isZoomEnable)
    val isZoomEnable: Boolean
        get() = mIsZoomEnable

    fun changeZoomState(enable: Boolean) {
        mIsZoomEnable = enable
    }

    internal var mScale by mutableStateOf(1f)
    val scale: Float
        get() = mScale

    internal var offset by mutableStateOf(Offset(0f, 0f))

    internal var mFile by mutableStateOf<File?>(null)
    val file: File?
        get() = mFile

    internal var pdfRender by mutableStateOf<BouquetPdfRender?>(null)

    internal var mLoadPercent by mutableStateOf(0)
    val loadPercent: Int
        get() = mLoadPercent

    val pdfPageCount: Int
        get() = pdfRender?.pageCount ?: 0

    abstract val currentPage: Int

    val isLoaded
        get() = mFile != null

    abstract val isScrolling: Boolean

    fun close() {
        pdfRender?.close()
        pdfRender = null
    }
}
